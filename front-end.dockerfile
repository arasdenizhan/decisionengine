FROM node:21-alpine3.19

WORKDIR /app
ENV PATH /app/node_modules/.bin:$PATH
COPY ./front-end/decision-app/package.json .
COPY ./front-end/decision-app/package-lock.json .
RUN npm install -g npm@10.5.0
COPY ./front-end .
ENTRYPOINT ["npm","start"]