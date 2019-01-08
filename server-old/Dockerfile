FROM node:10-stretch

RUN npm install typescript tsc -g

WORKDIR /home/node/

COPY package.json /home/node/
RUN npm install

COPY . /home/node
RUN chmod +x setup.sh && \
    tsc

# There used to be a 'WORKDIR /home/node/dist' instruction on this line.
# The reason it's taken out is that we need to call the 'node' command from the directory the .env file is in.
# We could still cd into dist/ and run 'node app.js' from there, but we'd first need to run 'cp .env dist/.env'
# Figured it was just simpler this way.

EXPOSE 3000

CMD [ "./setup.sh" ]
