/* eslint-disable @typescript-eslint/no-require-imports */
const fs = require("fs");
const path = require("path");
const { execSync } = require("child_process");

// Lê a versão a partir do pom.xml (resolvendo ${...} via <properties>)
function getVersionFromPom(pomPath) {
  const absPath = path.isAbsolute(pomPath)
    ? pomPath
    : path.join(__dirname, pomPath);

  let xml;
  try {
    xml = fs.readFileSync(absPath, "utf8");
  } catch (e) {
    console.error(`❌ Não foi possível ler o pom.xml em: ${absPath}`);
    throw e;
  }

  // Remove comentários para evitar ruído
  const noComments = xml.replace(/<!--[\s\S]*?-->/g, "");

  // Mapa de propriedades
  const props = {};
  const propsBlockMatch = noComments.match(
    /<properties>([\s\S]*?)<\/properties>/,
  );
  if (propsBlockMatch) {
    const block = propsBlockMatch[1];
    const propRegex = /<([\w\.-]+)>([\s\S]*?)<\/\1>/g;
    let m;
    while ((m = propRegex.exec(block)) !== null) {
      props[m[1]] = (m[2] || "").trim();
    }
  }

  // Remove blocos que contêm <version> que não são do nível do projeto
  let cleaned = noComments
    .replace(/<parent>[\s\S]*?<\/parent>/g, "")
    .replace(/<dependencies>[\s\S]*?<\/dependencies>/g, "")
    .replace(/<dependencyManagement>[\s\S]*?<\/dependencyManagement>/g, "")
    .replace(/<build>[\s\S]*?<\/build>/g, "")
    .replace(/<profiles>[\s\S]*?<\/profiles>/g, "")
    .replace(/<modules>[\s\S]*?<\/modules>/g, "");

  // Primeiro <version> restante deve ser a versão do projeto
  let versionMatch = cleaned.match(/<version>\s*([^<]+?)\s*<\/version>/);
  let version = versionMatch ? versionMatch[1].trim() : null;

  // Resolve placeholder ${...} via propriedades, ex.: ${revision}
  if (version && /^\$\{[^}]+\}$/.test(version)) {
    const propName = version.slice(2, -1);
    if (props[propName]) {
      version = props[propName];
    }
  }

  // Fallback: tenta ler do target/maven-archiver/pom.properties (se já houve build)
  if (!version || /^\$\{[^}]+\}$/.test(version)) {
    try {
      const propsPath = path.join(
        __dirname,
        "target",
        "maven-archiver",
        "pom.properties",
      );
      const content = fs.readFileSync(propsPath, "utf8");
      const line = content.split(/\r?\n/).find((l) => l.startsWith("version="));
      if (line) {
        version = line.split("=")[1].trim();
      }
    } catch {
      // ignore
    }
  }

  if (!version) {
    throw new Error(`❌ Versão não encontrada no pom.xml em: ${absPath}`);
  }

  return version;
}

const version = getVersionFromPom("./pom.xml");

console.log(`Versão detectada no pom.xml: ${version}`);

// Apenas para pegar o arquivo de env correto, o build ainda sera otimizado para produção
const MODE = "production";
const IMAGE_NAME =
  "registry.vitorweirich.com/praticas-extensionistas-doacoes-backend";
const TAG = `${IMAGE_NAME}:${version}`;

// Detecta flag --push
const PUSH = process.argv.includes("--push");
// Flag utilitária para apenas imprimir a versão
if (process.argv.includes("--print-version")) {
  console.log(version);
  process.exit(0);
}

console.log(`📦 Buildando Docker image: ${TAG} com MODE=${MODE}...`);

try {
  execSync(
    `docker build -f Dockerfile.native --build-arg MODE=${MODE} -t ${TAG} -t ${IMAGE_NAME}:latest .`,
    {
      stdio: "inherit",
    },
  );
  console.log(`✅ Imagem criada e tags ['${TAG}', 'latest'] adicionadas.`);

  if (PUSH) {
    try {
      console.log("⬆️  Pushando imagens para o registry...");
      execSync(`docker push ${TAG}`, { stdio: "inherit" });
      execSync(`docker push ${IMAGE_NAME}:latest`, { stdio: "inherit" });
      console.log("✅ Imagens pushadas com sucesso.");
    } catch (errPush) {
      console.error("❌ Erro ao pushar as imagens:", errPush);
      process.exit(1);
    }
  } else {
    console.log(
      "⏭️  Push skipped. Run this script with --push to push images after build.",
    );
  }
} catch (err) {
  console.error("❌ Erro ao buildar a imagem Docker:", err);
  process.exit(1);
}
