# twitterThreadPool

I den här implementationen av uppgiften skapas det en trådpool som används då TwitterRepository gör anrop mot TwitterAPI:et.

Här nedan visas att alla tre trådar i trådpoolen används då tweetsen hämtas:
* Thread name: pool-1-thread-1
* Thread name: pool-1-thread-2
* Thread name: pool-1-thread-3
* Thread name: pool-1-thread-1
* Thread name: pool-1-thread-2
 

I övrigt fungerar tjänsten som den tidigare SpringBoot applikationen. Dock med Servletfiltret borttaget.
--------------------------------------------------------------------------------------------------------
Url för att köra tjänsten: http://127.0.0.1:8080/sortedTweets?twitterTag=yolo

Tjänsten är satt till att högst läsa in 500 tweets. Ut från tjänsten returneras en lista (högst 100 ord) med populäraste
orden, sorterat i fallande popularitetsordning.

Jag har inte satt upp någon Swaggerdokumentation. Följande felkoder kan man få tillbaka:

* 422     Inte rätt information i queryparametern eller queryparametern saknas
* 500     Returneras av servlerfilter om högsta antalet concurrent requests är uppnått
* 500     Om ett TwitterException fångats i koden. Kan vara olika orsaker.
* 509     Alla tillgängliga anrop till twitter är slut


