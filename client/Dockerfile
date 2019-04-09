# FROM node:10-alpine AS node

# WORKDIR /home/node

# COPY package.json /home/node
# RUN npm install

# COPY . /home/node
# RUN ./node_modules/@angular/cli/bin/ng build


# FROM nginx:latest

# WORKDIR /home
# COPY nginx.default.conf /etc/nginx/conf.d/default.conf
# COPY --from=node /home/node/dist/AppointmentScheduler/ /home/
# Stage 0, "build-stage", based on Node.js, to build and compile the frontend
FROM tiangolo/node-frontend:10 as build-stage
WORKDIR /app
COPY package*.json /app/
RUN npm install
COPY ./ /app/
ARG configuration=production
RUN npm run build -- --output-path=./dist/out --configuration $configuration

# Stage 1, based on Nginx, to have only the compiled app, ready for production with Nginx
FROM nginx:1.15
COPY --from=build-stage /app/dist/out/ /usr/share/nginx/html
# Copy the default nginx.conf provided by tiangolo/node-frontend
COPY --from=build-stage /nginx.conf /etc/nginx/conf.d/default.conf