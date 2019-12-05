# Stock Notify Microservices

**Note:** 
    * You will only need docker installed on your computer to run this app

## Git Steps
1. Fork Branch
2. Open terminal and clone **forked branch**: `git clone https://github.com/<YOUR USERNAME>/stock-notify.git`
3. Go inside templates directory: `cd templates`
3. Add upstream repo: `git remote add upstream https://github.com/fcgl/stock-notify.git`
4. Confirm that you have an origin and upstream repos: `git remote -v`

## Build & Run App

This template should work for both macOS and Linux

1. Download docker for your operating system
2. From project root run the following commands:
    * build and run: `docker-compose up --build`


## Health Endpoint

Confirm everything was ran correctly by going to the following endpoint: 
    * http://localhost:8086/health/v1/marco

## Changes Required For Your Project
1. Change Port from 8086 to #### in Dockerfile and docker-compose.yml
2. Change Project Name


