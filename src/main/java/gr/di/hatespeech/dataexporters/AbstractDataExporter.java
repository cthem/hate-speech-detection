package gr.di.hatespeech.dataexporters;

import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import javax.persistence.EntityManagerFactory;

import org.apache.log4j.PropertyConfigurator;

import gr.di.hatespeech.entities.Text;
import gr.di.hatespeech.features.BOWFeaturesExtractor;
import gr.di.hatespeech.features.CharacterNGramFeatureExtractor;
import gr.di.hatespeech.features.NgramFeatureExtractor;
import gr.di.hatespeech.features.SpellingFeatureExtractor;
import gr.di.hatespeech.features.SyntaxFeatureExtractor;
import gr.di.hatespeech.features.Word2VecFeatureExtractor;
import gr.di.hatespeech.utils.Utils;

public abstract class AbstractDataExporter<T> implements DataExporter<T> {
	private static String startingMessageLog = "[" + AbstractDataExporter.class.getSimpleName() + "] ";
	protected Properties config = new Properties();
	protected EntityManagerFactory factory;

	/** Feature extractors **/
	protected BOWFeaturesExtractor bowExtractor;
	protected Word2VecFeatureExtractor word2vecExtractor;
	protected NgramFeatureExtractor ngramFeatureExtractor;
	protected CharacterNGramFeatureExtractor charngramFeatureExtractor;
	protected SpellingFeatureExtractor spellingFeatureExtractor;
	protected SyntaxFeatureExtractor syntaxFeatureExtractor;
	
	public AbstractDataExporter() {
		readConfigurationFile();
		initFeatureExtractors();
	}

	protected void readConfigurationFile() {
		InputStream in;
		try {
			String propertiesName = Utils.CONFIG_FILE;
			in = getClass().getClassLoader().getResourceAsStream(propertiesName);
			config.load(in);
			PropertyConfigurator.configure(config);
		} catch (IOException | NullPointerException e) {
			Utils.FILE_LOGGER.error(startingMessageLog + e.getMessage(), e);
		}
	}
	
	protected void initFeatureExtractors() {
		bowExtractor = new BOWFeaturesExtractor(Utils.HATEBASE_CSV_PATH, Utils.BOW_KEY_PREFIX);
		word2vecExtractor = new Word2VecFeatureExtractor(config.getProperty(Utils.AGGREGATION_TYPE),
				Utils.WORD2VEC_KEY_PREFIX, Utils.WORD2VEC_SMALL_SER);
		ngramFeatureExtractor = new NgramFeatureExtractor(Utils.NGRAM_KEY_PREFIX);
		charngramFeatureExtractor = new CharacterNGramFeatureExtractor(Utils.CHAR_NGRAM_KEY_PREFIX);
		spellingFeatureExtractor = new SpellingFeatureExtractor(Utils.ENGLISH_DICTIONARY_PATH,
				Utils.SPELLING_KEY_PREFIX);
		syntaxFeatureExtractor = new SyntaxFeatureExtractor(Utils.ENGLISH_PCFG, Utils.SYNTAX_KEY_PREFIX,
				Utils.SENTIMENT_KEY_PREFIX);
	}
	
	public Map<String, Double> getVectorFeatures(Text text) {
		Map<String, Double> textFeatures = new HashMap<>();
		if (Boolean.parseBoolean(config.getProperty(Utils.BOW))) {
			textFeatures.putAll(bowExtractor.extractFeatures(text));
		}
		if (Boolean.parseBoolean(config.getProperty(Utils.WORD2VEC))) {
			textFeatures.putAll(word2vecExtractor.extractFeatures(text));
		}
		if (Boolean.parseBoolean(config.getProperty(Utils.NGRAM))) {
			textFeatures.putAll(ngramFeatureExtractor.extractFeatures(text));
		}
		if (Boolean.parseBoolean(config.getProperty(Utils.CHAR_NGRAM))) {
			textFeatures.putAll(charngramFeatureExtractor.extractFeatures(text));
		}
		if (Boolean.parseBoolean(config.getProperty(Utils.SPELLING))) {
			textFeatures.putAll(spellingFeatureExtractor.extractFeatures(text));
		}
		if (Boolean.parseBoolean(config.getProperty(Utils.SYNTAX))) {
			textFeatures.putAll(syntaxFeatureExtractor.extractFeatures(text));
		}
		return textFeatures;
	}
	
	@Override
	public void exportDataToCsv(List<T> data, String[] headerRecord, String fileName, CsvOptions options) {
		
	}

	@Override
	public void exportDataToDatabase(List<T> data) {
		
	}
	
	public Properties getConfig() {
		return config;
	}

	public void setConfig(Properties config) {
		this.config = config;
	}

	public EntityManagerFactory getFactory() {
		return factory;
	}

	public void setFactory(EntityManagerFactory factory) {
		this.factory = factory;
	}

	public BOWFeaturesExtractor getBowExtractor() {
		return bowExtractor;
	}

	public void setBowExtractor(BOWFeaturesExtractor bowExtractor) {
		this.bowExtractor = bowExtractor;
	}

	public Word2VecFeatureExtractor getWord2vecExtractor() {
		return word2vecExtractor;
	}

	public void setWord2vecExtractor(Word2VecFeatureExtractor word2vecExtractor) {
		this.word2vecExtractor = word2vecExtractor;
	}

	public NgramFeatureExtractor getNgramFeatureExtractor() {
		return ngramFeatureExtractor;
	}

	public void setNgramFeatureExtractor(NgramFeatureExtractor ngramFeatureExtractor) {
		this.ngramFeatureExtractor = ngramFeatureExtractor;
	}

	public CharacterNGramFeatureExtractor getCharngramFeatureExtractor() {
		return charngramFeatureExtractor;
	}

	public void setCharngramFeatureExtractor(CharacterNGramFeatureExtractor charngramFeatureExtractor) {
		this.charngramFeatureExtractor = charngramFeatureExtractor;
	}

	public SpellingFeatureExtractor getSpellingFeatureExtractor() {
		return spellingFeatureExtractor;
	}

	public void setSpellingFeatureExtractor(SpellingFeatureExtractor spellingFeatureExtractor) {
		this.spellingFeatureExtractor = spellingFeatureExtractor;
	}

	public SyntaxFeatureExtractor getSyntaxFeatureExtractor() {
		return syntaxFeatureExtractor;
	}

	public void setSyntaxFeatureExtractor(SyntaxFeatureExtractor syntaxFeatureExtractor) {
		this.syntaxFeatureExtractor = syntaxFeatureExtractor;
	}

}
