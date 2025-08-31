/* eslint-disable @typescript-eslint/no-require-imports */
const { version } = require("./package.json");
const { execSync } = require("child_process");

// Apenas para pegar o arquivo de env correto, o build ainda sera otimizado para produção
const MODE = "production";
const IMAGE_NAME =
  "registry.vitorweirich.com/praticas-extensionistas-doacoes-frontend";
const TAG = `${IMAGE_NAME}:${version}`;

console.log(`📦 Buildando Docker image: ${TAG} com MODE=${MODE}...`);

try {
  execSync(
    `docker build --build-arg MODE=${MODE} -t ${TAG} -t ${IMAGE_NAME}:latest .`,
    {
      stdio: "inherit",
    }
  );
  console.log(`✅ Imagem criada e tags ['${TAG}', 'latest'] adicionadas.`);
} catch (err) {
  console.error("❌ Erro ao buildar a imagem Docker:", err);
  process.exit(1);
}
