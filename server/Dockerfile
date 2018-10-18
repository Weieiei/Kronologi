FROM node:10-stretch

EXPOSE 3000

WORKDIR /home/node
COPY . /home/node/
RUN npm install

CMD [ "node", "app.js" ]
