FROM node:10-stretch

# Copy repo
WORKDIR /home/node
COPY . /home/node/
RUN npm install

EXPOSE 3000

CMD [ "node", "app.js" ]
