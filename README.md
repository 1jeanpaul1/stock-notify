# Stock Notify Microservices

`Purpose`: Send an SMS notification to a user when a keyword is triggered in a headline.

`Background`:   There has been a recent trend of  Biotech companies increasing in value after releasing a positive result for a treatment/drug during one of their fda phases. Someone needs to keep refreshing the the webpage of website that publish the companies press releases in order to stay up to date. This takes that out of the equation by automatically notifying the user when needed. 

`Implementation`: I found that `business wire` and `global news wire` have the majority of the important press releases that are needed. These website are scraped for new articles every `x` amount of time (depends on the setting you set). The headlines are parsed for the keywords. Once triggered, the article url and headline are sent to the user.

    * bitly is not necessarily required for this implementation. However sms has a limit of 160 character. At times these characters are greater than 160 characters causing the link to be cut in two. Bitly solves this issue by making the link a lot smaller. Saves money on sending multiple texts as well. 
    * twilio is used to send text messages
    * It is possible to have twilio and bitly for free. If the user creates multiple accounts and adds these accounts to a list that is sequntially used for their respective purposes. The user could theoretically have these services for free if unlimited accounts were available. You get $15 in free credit from twililo at $0.0015 per sms, and 1000 free links from bitly


**Note:** 
    * You will only need docker and docker-compose installed on your computer to run this app

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
    * http://localhost:8096/health/v1/marco
    


