FROM node:10-alpine AS node

WORKDIR /home/node


RUN apk add --no-cache git
COPY package.json /home/node
RUN npm install

COPY . /home/node
RUN ./node_modules/@angular/cli/bin/ng build


FROM nginx:latest

WORKDIR /home
COPY nginx.default.conf /etc/nginx/conf.d/default.conf
COPY --from=node /home/node/dist/AppointmentScheduler/ /home/
