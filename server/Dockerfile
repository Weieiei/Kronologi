FROM node:10-stretch

RUN npm install typescript tsc -g

WORKDIR /home/node/

COPY package.json /home/node/
RUN npm install

COPY . /home/node
RUN chmod +x setup.sh && \
    tsc

WORKDIR /home/node/dist

EXPOSE 3000

CMD [ "../setup.sh" ]
