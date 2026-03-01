# BUILD.md – Comandos Úteis para Build e Execução do App Mobile

## 1. Instalação de Dependências

```sh
npm install
```

## 2. Build do APK Android

### Usando o script customizado (recomendado para build local com Gradle)

```sh
node build-android.js
```

### Usando Expo CLI (modo desenvolvimento)

```sh
npm run android
```

Ou, diretamente:

```sh
npx expo start --android
```

## 3. Setup de Emulador Android

### 3.1. Instalar Android Studio e SDK

- Baixe e instale o [Android Studio](https://developer.android.com/studio)
- Instale o Android SDK e o AVD Manager

### 3.2. Criar um Emulador (AVD)

1. Abra o Android Studio > Device Manager
2. Clique em "Create Device"
3. Escolha um modelo (ex: Pixel 5)
4. Selecione uma imagem do sistema (API 33+ recomendado)
5. Finalize e inicie o emulador

### 3.3. Iniciar o Emulador via Terminal

```sh
emulator -list-avds
emulator -avd <NOME_DO_EMULADOR>
```

Exemplo:

```sh
emulator -avd Medium_Phone_API_36.0
```

## 4. Comandos Úteis

### Verificar dispositivos conectados

```sh
adb devices
```

### Instalar APK manualmente

```sh
adb install <caminho_para_apk>
```

### Limpar build do projeto

```sh
npx expo start -c
```

---

**Dicas:**

- Certifique-se de que o emulador está rodando antes de executar `npm run android`.
- Para builds de produção, utilize o script `build-android.js` para gerar o APK assinado.
- Use o comando `adb logcat` para debugar logs do app no emulador.
