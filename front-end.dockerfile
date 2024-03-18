FROM node:21-alpine3.19

WORKDIR /app
ENV PATH /app/node_modules/.bin:$PATH
COPY ./front-end/decision-app/package.json .
COPY ./front-end/decision-app/package-lock.json .
RUN npm ci
COPY ./front-end/decision-app .
ENTRYPOINT ["npm","start"]