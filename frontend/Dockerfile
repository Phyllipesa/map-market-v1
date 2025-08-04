# Etapa 1: Build da aplicação Angular
FROM node:20 AS builder
WORKDIR /app
COPY . .

# Recebe API_URL via build arg e substitui no environment.prod.ts
ARG API_URL
RUN sed -i "s|API_URL_PLACEHOLDER|${API_URL}|g" src/environments/environment.prod.ts

RUN npm install
RUN npm run build --no-cache

# Etapa 2: Servir a aplicação com Nginx
FROM nginx:alpine
COPY --from=builder /app/dist/mapMarketFV1/browser /usr/share/nginx/html
COPY nginx.conf /etc/nginx/nginx.conf
COPY mime.types /etc/nginx/mime.types
EXPOSE 4200
CMD ["nginx", "-g", "daemon off;"]
