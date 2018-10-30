FROM node:10-alpine AS node

# Install git and openssh (for git ssh)
RUN apk update && apk add --no-cache git openssh

# Add credentials on build
ARG SSH_PRIVATE_KEY_PATH=.secrets/ssh/private_key
ARG SSH_PUBLIC_KEY_PATH=.secrets/ssh/public_key.pub
RUN mkdir /root/.ssh
COPY $SSH_PRIVATE_KEY_PATH /root/.ssh/id_rsa
COPY $SSH_PUBLIC_KEY_PATH /root/.ssh/id_rsa.pub
RUN chmod 600 ~/.ssh/id_rsa ~/.ssh/id_rsa.pub
RUN ssh-keyscan github.com > /root/.ssh/known_hosts

# Clone repo
WORKDIR /home/node
RUN git clone git@github.com:vartanbeno/AppointmentScheduler.git
WORKDIR /home/node/AppointmentScheduler/client
RUN npm install && ./node_modules/@angular/cli/bin/ng build


FROM nginx:latest

WORKDIR /home
COPY nginx.default.conf /etc/nginx/conf.d/default.conf
COPY --from=node /home/node/AppointmentScheduler/client/dist/AppointmentScheduler/* /home/
