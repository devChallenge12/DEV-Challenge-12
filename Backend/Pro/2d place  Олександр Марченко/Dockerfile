FROM node:alpine

WORKDIR /app
COPY package*.json ./
RUN npm install -s
COPY . .

EXPOSE 3000

CMD node server.js