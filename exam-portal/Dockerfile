FROM nginx
EXPOSE 80
ADD nginx.conf /etc/nginx/nginx.conf
COPY /dist /usr/share/nginx/html
ENTRYPOINT nginx -g "daemon off;"