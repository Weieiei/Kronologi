FROM node:10-alpine AS node

# Copy repo
WORKDIR /home/node
COPY . /home/node
RUN npm install && ./node_modules/@angular/cli/bin/ng build


FROM nginx:latest

WORKDIR /home
COPY nginx.default.conf /etc/nginx/conf.d/default.conf
COPY --from=node /home/node/dist/AppointmentScheduler/* /home/
