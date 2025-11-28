FROM node:20-alpine AS build
WORKDIR /app
COPY package*.json ./
RUN npm install
COPY . .
RUN npm run build

FROM nginx:alpine

# Supprimer la config nginx par défaut
RUN rm -rf /usr/share/nginx/html/* && \
  rm /etc/nginx/conf.d/default.conf

# Copier les fichiers Angular buildés
COPY --from=build /app/dist/business-case-front/ /usr/share/nginx/html

# Copier la configuration nginx custom
COPY nginx.conf /etc/nginx/conf.d/default.conf

# Définir les permissions correctes
RUN chmod -R 755 /usr/share/nginx/html && \
  chown -R nginx:nginx /usr/share/nginx/html

EXPOSE 80

# Health check
HEALTHCHECK --interval=30s --timeout=3s --start-period=5s --retries=3 \
  CMD wget --quiet --tries=1 --spider http://localhost/health || exit 1

CMD ["nginx", "-g", "daemon off;"]
