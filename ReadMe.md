# Dev Instructions

## Files
* Copy the persistence-example.xml (src/main/resources/META-INF) and rename it as persistence.xml. In this file, 
change the database name (replace the DATABASE in line 9) and add your credentials in username and password. 
Finally, you need to add MySQL driver in the project's classpath and configure the project as a JPA project, 
adding the database source and the driver.
* Get the database from here: https://www.dropbox.com/sh/xt7ka9m3jdq9cpf/AAAtPW5JxMBAsxdUZ7ctGiwga?dl=0
* Get the jar files for JInsect and OpenJGraph from here: https://www.dropbox.com/sh/yya619oixlfk06n/AACwiFAtoblx4z5WTJn07mfua?dl=0
* Database data in csv format from here: https://www.dropbox.com/sh/2i1iafx8d3l8xnm/AACzDR9H3NuITCELi8_86YPpa?dl=0
* If you want to re-create the features and instances, copy below folders in src/main/resources :
    * BOW features: https://www.dropbox.com/sh/j2qqcimaqtcox2g/AADhkkPWQCQBg_IMMmsp_6ANa?dl=0
    * ngrams: https://www.dropbox.com/sh/qoj1405kt1alb4v/AACHXFeKQfadhQxEh3pA9B81a?dl=0
    * Spelling: https://www.dropbox.com/sh/mfejbropwdakot7/AAArF4f7olFkkEQCWrlJZScGa?dl=0
    * Preprocessing: https://www.dropbox.com/sh/ghps231vbkinztf/AADk-P3HoKXy93tRP_iffAuWa?dl=0
    * word2vec: https://www.dropbox.com/sh/dynm29i7430h2g6/AAAbxOeOdIbOLFWk5kONS-Via?dl=0
* Also in the root folder of the project create a logs folder to save the program's logs.
* In case you do not want to produce new instances, you can find the instances I have produced here: https://www.dropbox.com/sh/sdifspbc374gewz/AAALrk3CddDIJxe_meU0mMKUa?dl=0

## Configurations

* twitter4j.properties: file needed in order to download tweets from the one of the two datasets used
* log4j.properties: file to configure logger
* emailConfig-example.properties: rename this file to emailConfig.properties and define your own properties to get notified when execution of the program is finished
* config.properties: 
	* parallel: run folds in parallel
	* numFolds: configuation used in cross validation to define the folds number
	* runs: used only for cross validation classificationType. Defines how many times cross validation will be executed
	* dataset: select -1 to include all texts and run the program as single label supervised learning, otherwise choose only one of the two datasets (put 0 or 1) to select only one dataset and run the program as multi label supervised learning
	* instances: you can either choose "new" to generate new instances or "existing" to use already extracted instances, which will be accessed from arff file
	* pathToInstances: since we have created instances for the merged dataset and for each dataset separately, define from which folder the program will retrieve the instances, e.g. "./instances/singlelabel/". You need to define only this part of the path, since the remaining is the same in all instances folders. The path is associated with the previous field.
	* datasource: you can choose either to access data (texts, features and texts_features) from the database or from csv files
	* vectorFeatures: same here, you can write "new" to re-generate vector features or use "existing" to access them from the database or the csv. In both cases you should first select new in instances (above) field
	* graphFeatures: use true/false in order to generate or not graphFeatures (true is meaningless if you have not chosen new instances)
	* graphType: define if it is ngram or word graph (select true for graphFeatures first)
	* featuresKind: it is related to vector features. One can select "all" or a specific kind (e.g. bow, ngrams etc)
	* instancesToFile: in case you have selected to generate new instances, select true or false to define whether the instances will be exported to file or not
	* Below vector features configurations are used only in case you have selected the "new" option in vectorFeatures field:
		* preprocess: select true or false to define if you want to preprocess your texts
		* stopwords: in case you have selected to preprocess the texts, define if you want to also remove stopwords
		* bow: generate or not bow features
		* word2vec: generate or not word2vec features
		* aggregationType: define the aggregation type for word2vec features (this means that you have selected true in the above field)
		* charngram: generate or not charngram features
		* ngram: generate or not ngram features
		* spelling: generate or not spelling features
		* syntax: generate or not syntax features
	* classificationType: select either "classification" or "crossValidation"
	* Classifiers configuration: define which classifiers will run by selecting true/false in the fields NaiveBayes, LogisticRegression and KNN
	
## Datasets

* Single Label:
	* HateSpeech: 24463
	*  Clean: 14548
	* Total: 39011
* Multi Label (Racism, Sexism, Clean):
	* Racism: 1910
	* Sexism: 3035
   	* Clean: 10543
   	* Total: 15488
* Multi Label (HateSpeech, OffensiveLanguage, Clean):
   	* HateSpeech: 1392
   	* OffensiveLanguage: 18126
   	* Clean: 4005
   	* Total: 23523

## Classification

* Problem with KNN: tested for training in Weka GUI, with n=3 training is quick while with n=9 takes time
   	
## Metrics

* F-Measure: calculated using function weightedFMeasure which calculates the average F-Measure.
* Kappa: metric that compares an Observed Accuracy with an Expected Accuracy (random chance). Used to evaluate a single classifier as well as to evaluate classifiers amongst themselves. Also, takes into account random chance which generally means it is less misleading than simply use accuracy as a metric (an Observed Accuracy of 80% is a lot less impressive with an Expected Accuracy of 75% versus an Expected Accuracy of 50%). Observed Accuracy is simply the number of instances that were classified correctly throughout the entire confusion matrix. Expected Accuracy is defined as the accuracy that any random classifier would be expected to achieve based on the confusion matrix. The Expected Accuracy is directly related to the number of instances of each class