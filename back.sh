gcloud init
gcloud app create
gcloud auth application-default login

git clone https://github.com/pribytkovskiy/bikeChampionship.git
cd bikeChampionship
sh ./mvnw install
sh ./mvnw spring-boot:run -f ./server/