# AirQualityMonitor
An app to show live tracking of air quality index for Indian major cities.

## ScreenShots

### Main Activity
![1st_Activity](https://user-images.githubusercontent.com/13044058/123695285-fea70a80-d877-11eb-9208-10ad8e343d02.png)

### Graph Activity
![2nd_acttivity](https://user-images.githubusercontent.com/13044058/123695316-09fa3600-d878-11eb-90dc-7597e54f8b03.png)


## Apk Path
app/build/outputs/apk/debug/app-debug.apk

## Important Android Components & Libraries Used

Java-WebSocket : To establish and send and receive messages from websockets connetion.

Databinding : To bind UI components to XML layout.

Gson : To parse message received from websocket.

Room : For querying database.

ViewModel : For managing UI-related data in a lifecycle conscious way.

LiveData : For holding observable data.

RecyclerView : To display list of items.

Spark : To show graph of aqi data.

Coroutines : To delegate task to worker thread.

## Architecture

The project uses MVVM architecture.

![mvvm-arch](https://user-images.githubusercontent.com/13044058/123699994-88a5a200-d87d-11eb-9c56-6542f7af938a.png)

### Activites

- Main Activity: Displays list of cites and their latest air quality index.
- Graph Activty: Shows sparkline graph for historical data upto last 1 min.


The websocket messages are stored in the ROOM ORM. AqiViewModel loads the data from the ROOM ORM and refreshes the UI every 30 secs. This is applicable for both MainActivity and GraphActivity. 

Room ORM retains the data of last one minute. This could be configured and changed to required retention policy for AQI data.

## Feature build vs Time Taken

WebSocket Connection -> 45 min

Main Activity Development-> 2 hr

SQL/RoomOrm -> 45 min

Graph Activity Development -> 1 hr

Code Review & Refactor -> 45 min
