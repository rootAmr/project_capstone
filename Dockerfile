FROM node:21

WORKDIR /app

ENV PORT 8080

ENV GOOGLE_APPLICATION_CREDENTIALS serviceAccount.json

ENV DATABASE_URL "mysql://groot:foodmood15@34.101.217.134:3306/db-fomo-cs"

COPY . .

RUN npm install

RUN npx prisma generate

EXPOSE 8080

CMD ["npm", "run", "start"]