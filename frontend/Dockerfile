# Use an official Node.js runtime as the base image
FROM node:14-alpine

RUN pwd && ls

# Set the working directory inside the container
WORKDIR /app

RUN pwd && ls

# Copy package.json and package-lock.json to the container
COPY package*.json ./

# Install dependencies
RUN npm install

# Copy the rest of the application code to the container
COPY . .

# Build the React app
RUN npm run build

# Expose the port the app runs on
EXPOSE 3000

# Start the React app
CMD ["npm", "start"]
