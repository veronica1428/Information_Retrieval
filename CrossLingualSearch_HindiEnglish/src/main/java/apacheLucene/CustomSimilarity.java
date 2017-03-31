package apacheLucene;

import org.apache.lucene.search.similarities.BasicStats;
import org.apache.lucene.search.similarities.LMJelinekMercerSimilarity;
import org.apache.lucene.search.similarities.LMSimilarity;

public class CustomSimilarity extends LMJelinekMercerSimilarity{

	private final float lambda;
	 
	public CustomSimilarity(float lambda){
		super(lambda);
		this.lambda = lambda;
	}
	
	public float score(BasicStats stats, float freq, float docLen){
		
		float firstTerm = 0.0F;
		float secondTerm = 0.0F;
		float totalLMProb = 0.0F;
		
		firstTerm = (this.lambda)*(freq/docLen);
		secondTerm = (1F - this.lambda)*(((LMSimilarity.LMStats)stats).getCollectionProbability());
		
		totalLMProb = stats.getTotalBoost()*(firstTerm + secondTerm);
		return totalLMProb;	
	}
}
