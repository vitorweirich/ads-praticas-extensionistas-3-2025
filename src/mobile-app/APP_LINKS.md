# App Links – Alterações e Próximos Passos

## Alterações Realizadas

### 1. Configuração do App Links (Android)

- **Arquivo `app.json`**
  - Adicionado o bloco `intentFilters` na configuração Android, permitindo que o app responda a links HTTPS do domínio especificado (`gm8p7wgv-3000.brs.devtunnels.ms`) com o prefixo `/app`.
  - Exemplo:
    ```json
    "intentFilters": [
      {
        "action": "VIEW",
        "autoVerify": true,
        "data": [
          {
            "scheme": "https",
            "host": "gm8p7wgv-3000.brs.devtunnels.ms",
            "pathPrefix": "/app"
          }
        ],
        "category": ["BROWSABLE", "DEFAULT"]
      }
    ]
    ```

### 2. Publicação do arquivo de verificação

- **Arquivo `assetlinks.json`**
  - Criado em `src/frontend/public/.well-known/assetlinks.json`.
  - Permite que o Android valide a relação entre o app e o domínio, usando o fingerprint da chave de assinatura.
  - Exemplo:
    ```json
    [
      {
        "relation": ["delegate_permission/common.handle_all_urls"],
        "target": {
          "namespace": "android_app",
          "package_name": "com.anonymous.mobileapp",
          "sha256_cert_fingerprints": [
            "FA:C6:17:45:DC:09:03:78:6F:B9:ED:E6:2A:96:2B:39:9F:73:48:F0:BB:6F:89:9B:83:32:66:75:91:03:3B:9C"
          ]
        }
      }
    ]
    ```

### 3. Documentação

- Comentário adicionado em `build-android.js` sobre como obter o fingerprint da chave de assinatura usando o comando `keytool`.

---

## O que ainda falta fazer?

1. **Hospedar o site com HTTPS**
   - O arquivo `assetlinks.json` precisa estar disponível publicamente em `https://<domínio>/.well-known/assetlinks.json`.
   - O domínio usado no intent filter deve ser o mesmo do site hospedado.

2. **Validar a configuração no dispositivo**
   - Após publicar o site, execute:
     ```sh
     adb shell pm get-app-links com.anonymous.mobileapp
     ```
   - O status deve indicar que o domínio está verificado.

3. **Testar abertura de links**
   - Acesse um link do domínio pelo navegador do Android e verifique se o app é sugerido/aberto automaticamente.

---

**Resumo:**

- App Links configurado no app e no frontend.
- Falta hospedar o site em HTTPS e validar a associação no dispositivo Android.
