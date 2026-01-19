FROM node:20-alpine AS build

RUN mkdir /source
WORKDIR /source

COPY . /source

RUN npm install && npm run build

FROM nginx:alpine

COPY nginx.conf /etc/nginx/conf.d/default.conf

COPY --from=build /source/dist /usr/share/nginx/html
