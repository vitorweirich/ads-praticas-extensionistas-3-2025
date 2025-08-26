# Etapa 1: build do projeto
FROM node:22.17.1 AS builder

WORKDIR /app

# Copia apenas os arquivos de dependência
COPY package.json yarn.lock ./

# Instala dependências (usa cache se package.json e yarn.lock não mudarem)
RUN yarn

# Copia o restante dos arquivos do projeto
COPY . .

RUN yarn
ARG MODE=production
RUN yarn build --mode $MODE

# Etapa 2: imagem final leve com Nginx
FROM nginx:alpine

# Remove configuração default
RUN rm -rf /usr/share/nginx/html/*

# Copia o build do Vue para o Nginx
COPY --from=builder /app/dist /usr/share/nginx/html

COPY nginx.conf /etc/nginx/conf.d/default.conf

EXPOSE 3000

CMD ["nginx", "-g", "daemon off;"]
