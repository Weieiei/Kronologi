FROM node:10-stretch

# Copy repo
WORKDIR /home/node
COPY . /home/node/

RUN npm install\
        && npm install typescript -g\
        && npm run-script tsc;

WORKDIR /home/node/dist
# Make the script executable
RUN chmod +x ../setup.sh



EXPOSE 3000

CMD [ "../setup.sh" ]
