#!/usr/bin/env node
"use strict";
const { spawn } = require("child_process");
const path = require("path");

const root = __dirname;
const frontendScript = path.join(root, "frontend", "build-docker-image.cjs");
const backendScript = path.join(root, "backend", "build-docker-image.cjs");

function run(cmd, args, options = {}) {
  return new Promise((resolve, reject) => {
    const child = spawn(cmd, args, {
      stdio: "inherit",
      shell: true,
      ...options,
    });
    child.on("close", (code) => {
      if (code === 0) resolve();
      else
        reject(
          new Error(`Command "${cmd} ${args.join(" ")}" exited with ${code}`)
        );
    });
  });
}

(async () => {
  try {
    console.log("Running frontend build with --push...");

    await run("node", [frontendScript, "--push"], {
      cwd: path.dirname(frontendScript),
    });

    console.log("Frontend build succeeded.");

    console.log("Running backend build with --push...");

    await run("node", [backendScript, "--push"], {
      cwd: path.dirname(backendScript),
    });

    console.log("Backend build succeeded.");

    console.log("All builds completed successfully.");
    process.exit(0);
  } catch (err) {
    console.error(err.message || err);
    process.exit(1);
  }
})();
