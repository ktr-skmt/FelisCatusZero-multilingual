
/* First created by JCasGen Mon Feb 20 23:16:26 JST 2017 */
package jeqa.types;

import org.apache.uima.jcas.JCas;
import org.apache.uima.jcas.JCasRegistry;
import org.apache.uima.cas.impl.CASImpl;
import org.apache.uima.cas.impl.FSGenerator;
import org.apache.uima.cas.FeatureStructure;
import org.apache.uima.cas.impl.TypeImpl;
import org.apache.uima.cas.Type;
import org.apache.uima.cas.impl.FeatureImpl;
import org.apache.uima.cas.Feature;

/** CoreNLP Analysis
 * Updated by JCasGen Mon Feb 20 23:16:26 JST 2017
 * @generated */
public class CoreNLPAnalysis_Type extends Analysis_Type {
  /** @generated 
   * @return the generator for this type
   */
  @Override
  protected FSGenerator getFSGenerator() {return fsGenerator;}
  /** @generated */
  private final FSGenerator fsGenerator = 
    new FSGenerator() {
      public FeatureStructure createFS(int addr, CASImpl cas) {
  			 if (CoreNLPAnalysis_Type.this.useExistingInstance) {
  			   // Return eq fs instance if already created
  		     FeatureStructure fs = CoreNLPAnalysis_Type.this.jcas.getJfsFromCaddr(addr);
  		     if (null == fs) {
  		       fs = new CoreNLPAnalysis(addr, CoreNLPAnalysis_Type.this);
  			   CoreNLPAnalysis_Type.this.jcas.putJfsFromCaddr(addr, fs);
  			   return fs;
  		     }
  		     return fs;
        } else return new CoreNLPAnalysis(addr, CoreNLPAnalysis_Type.this);
  	  }
    };
  /** @generated */
  @SuppressWarnings ("hiding")
  public final static int typeIndexID = CoreNLPAnalysis.typeIndexID;
  /** @generated 
     @modifiable */
  @SuppressWarnings ("hiding")
  public final static boolean featOkTst = JCasRegistry.getFeatOkTst("jeqa.types.CoreNLPAnalysis");
 
  /** @generated */
  final Feature casFeat_tokenList;
  /** @generated */
  final int     casFeatCode_tokenList;
  /** @generated
   * @param addr low level Feature Structure reference
   * @return the feature value 
   */ 
  public int getTokenList(int addr) {
        if (featOkTst && casFeat_tokenList == null)
      jcas.throwFeatMissing("tokenList", "jeqa.types.CoreNLPAnalysis");
    return ll_cas.ll_getRefValue(addr, casFeatCode_tokenList);
  }
  /** @generated
   * @param addr low level Feature Structure reference
   * @param v value to set 
   */    
  public void setTokenList(int addr, int v) {
        if (featOkTst && casFeat_tokenList == null)
      jcas.throwFeatMissing("tokenList", "jeqa.types.CoreNLPAnalysis");
    ll_cas.ll_setRefValue(addr, casFeatCode_tokenList, v);}
    
   /** @generated
   * @param addr low level Feature Structure reference
   * @param i index of item in the array
   * @return value at index i in the array 
   */
  public int getTokenList(int addr, int i) {
        if (featOkTst && casFeat_tokenList == null)
      jcas.throwFeatMissing("tokenList", "jeqa.types.CoreNLPAnalysis");
    if (lowLevelTypeChecks)
      return ll_cas.ll_getRefArrayValue(ll_cas.ll_getRefValue(addr, casFeatCode_tokenList), i, true);
    jcas.checkArrayBounds(ll_cas.ll_getRefValue(addr, casFeatCode_tokenList), i);
	return ll_cas.ll_getRefArrayValue(ll_cas.ll_getRefValue(addr, casFeatCode_tokenList), i);
  }
   
  /** @generated
   * @param addr low level Feature Structure reference
   * @param i index of item in the array
   * @param v value to set
   */ 
  public void setTokenList(int addr, int i, int v) {
        if (featOkTst && casFeat_tokenList == null)
      jcas.throwFeatMissing("tokenList", "jeqa.types.CoreNLPAnalysis");
    if (lowLevelTypeChecks)
      ll_cas.ll_setRefArrayValue(ll_cas.ll_getRefValue(addr, casFeatCode_tokenList), i, v, true);
    jcas.checkArrayBounds(ll_cas.ll_getRefValue(addr, casFeatCode_tokenList), i);
    ll_cas.ll_setRefArrayValue(ll_cas.ll_getRefValue(addr, casFeatCode_tokenList), i, v);
  }
 
 
  /** @generated */
  final Feature casFeat_partOfSpeechList;
  /** @generated */
  final int     casFeatCode_partOfSpeechList;
  /** @generated
   * @param addr low level Feature Structure reference
   * @return the feature value 
   */ 
  public int getPartOfSpeechList(int addr) {
        if (featOkTst && casFeat_partOfSpeechList == null)
      jcas.throwFeatMissing("partOfSpeechList", "jeqa.types.CoreNLPAnalysis");
    return ll_cas.ll_getRefValue(addr, casFeatCode_partOfSpeechList);
  }
  /** @generated
   * @param addr low level Feature Structure reference
   * @param v value to set 
   */    
  public void setPartOfSpeechList(int addr, int v) {
        if (featOkTst && casFeat_partOfSpeechList == null)
      jcas.throwFeatMissing("partOfSpeechList", "jeqa.types.CoreNLPAnalysis");
    ll_cas.ll_setRefValue(addr, casFeatCode_partOfSpeechList, v);}
    
   /** @generated
   * @param addr low level Feature Structure reference
   * @param i index of item in the array
   * @return value at index i in the array 
   */
  public int getPartOfSpeechList(int addr, int i) {
        if (featOkTst && casFeat_partOfSpeechList == null)
      jcas.throwFeatMissing("partOfSpeechList", "jeqa.types.CoreNLPAnalysis");
    if (lowLevelTypeChecks)
      return ll_cas.ll_getRefArrayValue(ll_cas.ll_getRefValue(addr, casFeatCode_partOfSpeechList), i, true);
    jcas.checkArrayBounds(ll_cas.ll_getRefValue(addr, casFeatCode_partOfSpeechList), i);
	return ll_cas.ll_getRefArrayValue(ll_cas.ll_getRefValue(addr, casFeatCode_partOfSpeechList), i);
  }
   
  /** @generated
   * @param addr low level Feature Structure reference
   * @param i index of item in the array
   * @param v value to set
   */ 
  public void setPartOfSpeechList(int addr, int i, int v) {
        if (featOkTst && casFeat_partOfSpeechList == null)
      jcas.throwFeatMissing("partOfSpeechList", "jeqa.types.CoreNLPAnalysis");
    if (lowLevelTypeChecks)
      ll_cas.ll_setRefArrayValue(ll_cas.ll_getRefValue(addr, casFeatCode_partOfSpeechList), i, v, true);
    jcas.checkArrayBounds(ll_cas.ll_getRefValue(addr, casFeatCode_partOfSpeechList), i);
    ll_cas.ll_setRefArrayValue(ll_cas.ll_getRefValue(addr, casFeatCode_partOfSpeechList), i, v);
  }
 
 
  /** @generated */
  final Feature casFeat_lemmaList;
  /** @generated */
  final int     casFeatCode_lemmaList;
  /** @generated
   * @param addr low level Feature Structure reference
   * @return the feature value 
   */ 
  public int getLemmaList(int addr) {
        if (featOkTst && casFeat_lemmaList == null)
      jcas.throwFeatMissing("lemmaList", "jeqa.types.CoreNLPAnalysis");
    return ll_cas.ll_getRefValue(addr, casFeatCode_lemmaList);
  }
  /** @generated
   * @param addr low level Feature Structure reference
   * @param v value to set 
   */    
  public void setLemmaList(int addr, int v) {
        if (featOkTst && casFeat_lemmaList == null)
      jcas.throwFeatMissing("lemmaList", "jeqa.types.CoreNLPAnalysis");
    ll_cas.ll_setRefValue(addr, casFeatCode_lemmaList, v);}
    
   /** @generated
   * @param addr low level Feature Structure reference
   * @param i index of item in the array
   * @return value at index i in the array 
   */
  public int getLemmaList(int addr, int i) {
        if (featOkTst && casFeat_lemmaList == null)
      jcas.throwFeatMissing("lemmaList", "jeqa.types.CoreNLPAnalysis");
    if (lowLevelTypeChecks)
      return ll_cas.ll_getRefArrayValue(ll_cas.ll_getRefValue(addr, casFeatCode_lemmaList), i, true);
    jcas.checkArrayBounds(ll_cas.ll_getRefValue(addr, casFeatCode_lemmaList), i);
	return ll_cas.ll_getRefArrayValue(ll_cas.ll_getRefValue(addr, casFeatCode_lemmaList), i);
  }
   
  /** @generated
   * @param addr low level Feature Structure reference
   * @param i index of item in the array
   * @param v value to set
   */ 
  public void setLemmaList(int addr, int i, int v) {
        if (featOkTst && casFeat_lemmaList == null)
      jcas.throwFeatMissing("lemmaList", "jeqa.types.CoreNLPAnalysis");
    if (lowLevelTypeChecks)
      ll_cas.ll_setRefArrayValue(ll_cas.ll_getRefValue(addr, casFeatCode_lemmaList), i, v, true);
    jcas.checkArrayBounds(ll_cas.ll_getRefValue(addr, casFeatCode_lemmaList), i);
    ll_cas.ll_setRefArrayValue(ll_cas.ll_getRefValue(addr, casFeatCode_lemmaList), i, v);
  }
 
 
  /** @generated */
  final Feature casFeat_stemmedWordList;
  /** @generated */
  final int     casFeatCode_stemmedWordList;
  /** @generated
   * @param addr low level Feature Structure reference
   * @return the feature value 
   */ 
  public int getStemmedWordList(int addr) {
        if (featOkTst && casFeat_stemmedWordList == null)
      jcas.throwFeatMissing("stemmedWordList", "jeqa.types.CoreNLPAnalysis");
    return ll_cas.ll_getRefValue(addr, casFeatCode_stemmedWordList);
  }
  /** @generated
   * @param addr low level Feature Structure reference
   * @param v value to set 
   */    
  public void setStemmedWordList(int addr, int v) {
        if (featOkTst && casFeat_stemmedWordList == null)
      jcas.throwFeatMissing("stemmedWordList", "jeqa.types.CoreNLPAnalysis");
    ll_cas.ll_setRefValue(addr, casFeatCode_stemmedWordList, v);}
    
   /** @generated
   * @param addr low level Feature Structure reference
   * @param i index of item in the array
   * @return value at index i in the array 
   */
  public int getStemmedWordList(int addr, int i) {
        if (featOkTst && casFeat_stemmedWordList == null)
      jcas.throwFeatMissing("stemmedWordList", "jeqa.types.CoreNLPAnalysis");
    if (lowLevelTypeChecks)
      return ll_cas.ll_getRefArrayValue(ll_cas.ll_getRefValue(addr, casFeatCode_stemmedWordList), i, true);
    jcas.checkArrayBounds(ll_cas.ll_getRefValue(addr, casFeatCode_stemmedWordList), i);
	return ll_cas.ll_getRefArrayValue(ll_cas.ll_getRefValue(addr, casFeatCode_stemmedWordList), i);
  }
   
  /** @generated
   * @param addr low level Feature Structure reference
   * @param i index of item in the array
   * @param v value to set
   */ 
  public void setStemmedWordList(int addr, int i, int v) {
        if (featOkTst && casFeat_stemmedWordList == null)
      jcas.throwFeatMissing("stemmedWordList", "jeqa.types.CoreNLPAnalysis");
    if (lowLevelTypeChecks)
      ll_cas.ll_setRefArrayValue(ll_cas.ll_getRefValue(addr, casFeatCode_stemmedWordList), i, v, true);
    jcas.checkArrayBounds(ll_cas.ll_getRefValue(addr, casFeatCode_stemmedWordList), i);
    ll_cas.ll_setRefArrayValue(ll_cas.ll_getRefValue(addr, casFeatCode_stemmedWordList), i, v);
  }
 



  /** initialize variables to correspond with Cas Type and Features
	 * @generated
	 * @param jcas JCas
	 * @param casType Type 
	 */
  public CoreNLPAnalysis_Type(JCas jcas, Type casType) {
    super(jcas, casType);
    casImpl.getFSClassRegistry().addGeneratorForType((TypeImpl)this.casType, getFSGenerator());

 
    casFeat_tokenList = jcas.getRequiredFeatureDE(casType, "tokenList", "uima.cas.FSArray", featOkTst);
    casFeatCode_tokenList  = (null == casFeat_tokenList) ? JCas.INVALID_FEATURE_CODE : ((FeatureImpl)casFeat_tokenList).getCode();

 
    casFeat_partOfSpeechList = jcas.getRequiredFeatureDE(casType, "partOfSpeechList", "uima.cas.FSArray", featOkTst);
    casFeatCode_partOfSpeechList  = (null == casFeat_partOfSpeechList) ? JCas.INVALID_FEATURE_CODE : ((FeatureImpl)casFeat_partOfSpeechList).getCode();

 
    casFeat_lemmaList = jcas.getRequiredFeatureDE(casType, "lemmaList", "uima.cas.FSArray", featOkTst);
    casFeatCode_lemmaList  = (null == casFeat_lemmaList) ? JCas.INVALID_FEATURE_CODE : ((FeatureImpl)casFeat_lemmaList).getCode();

 
    casFeat_stemmedWordList = jcas.getRequiredFeatureDE(casType, "stemmedWordList", "uima.cas.FSArray", featOkTst);
    casFeatCode_stemmedWordList  = (null == casFeat_stemmedWordList) ? JCas.INVALID_FEATURE_CODE : ((FeatureImpl)casFeat_stemmedWordList).getCode();

  }
}



    