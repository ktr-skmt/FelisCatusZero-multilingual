

/* First created by JCasGen Mon Feb 20 05:29:01 JST 2017 */
package jeqa.types;

import org.apache.uima.jcas.JCas; 
import org.apache.uima.jcas.JCasRegistry;
import org.apache.uima.jcas.cas.TOP_Type;

import org.apache.uima.jcas.cas.FSArray;


/** CoreNLP Analysis
 * Updated by JCasGen Mon Feb 20 05:29:01 JST 2017
 * XML source: src/main/resources/desc/ts/typeSystem.xml
 * @generated */
public class CoreNLPAnalysis extends Analysis {
  /** @generated
   * @ordered 
   */
  @SuppressWarnings ("hiding")
  public final static int typeIndexID = JCasRegistry.register(CoreNLPAnalysis.class);
  /** @generated
   * @ordered 
   */
  @SuppressWarnings ("hiding")
  public final static int type = typeIndexID;
  /** @generated
   * @return index of the type  
   */
  @Override
  public              int getTypeIndexID() {return typeIndexID;}
 
  /** Never called.  Disable default constructor
   * @generated */
  protected CoreNLPAnalysis() {/* intentionally empty block */}
    
  /** Internal - constructor used by generator 
   * @generated
   * @param addr low level Feature Structure reference
   * @param type the type of this Feature Structure 
   */
  public CoreNLPAnalysis(int addr, TOP_Type type) {
    super(addr, type);
    readObject();
  }
  
  /** @generated
   * @param jcas JCas to which this Feature Structure belongs 
   */
  public CoreNLPAnalysis(JCas jcas) {
    super(jcas);
    readObject();   
  } 

  /** @generated
   * @param jcas JCas to which this Feature Structure belongs
   * @param begin offset to the begin spot in the SofA
   * @param end offset to the end spot in the SofA 
  */  
  public CoreNLPAnalysis(JCas jcas, int begin, int end) {
    super(jcas);
    setBegin(begin);
    setEnd(end);
    readObject();
  }   

  /** 
   * <!-- begin-user-doc -->
   * Write your own initialization here
   * <!-- end-user-doc -->
   *
   * @generated modifiable 
   */
  private void readObject() {/*default - does nothing empty block */}
     
 
    
  //*--------------*
  //* Feature: tokenList

  /** getter for tokenList - gets 
   * @generated
   * @return value of the feature 
   */
  public FSArray getTokenList() {
    if (CoreNLPAnalysis_Type.featOkTst && ((CoreNLPAnalysis_Type)jcasType).casFeat_tokenList == null)
      jcasType.jcas.throwFeatMissing("tokenList", "jeqa.types.CoreNLPAnalysis");
    return (FSArray)(jcasType.ll_cas.ll_getFSForRef(jcasType.ll_cas.ll_getRefValue(addr, ((CoreNLPAnalysis_Type)jcasType).casFeatCode_tokenList)));}
    
  /** setter for tokenList - sets  
   * @generated
   * @param v value to set into the feature 
   */
  public void setTokenList(FSArray v) {
    if (CoreNLPAnalysis_Type.featOkTst && ((CoreNLPAnalysis_Type)jcasType).casFeat_tokenList == null)
      jcasType.jcas.throwFeatMissing("tokenList", "jeqa.types.CoreNLPAnalysis");
    jcasType.ll_cas.ll_setRefValue(addr, ((CoreNLPAnalysis_Type)jcasType).casFeatCode_tokenList, jcasType.ll_cas.ll_getFSRef(v));}    
    
  /** indexed getter for tokenList - gets an indexed value - 
   * @generated
   * @param i index in the array to get
   * @return value of the element at index i 
   */
  public Token getTokenList(int i) {
    if (CoreNLPAnalysis_Type.featOkTst && ((CoreNLPAnalysis_Type)jcasType).casFeat_tokenList == null)
      jcasType.jcas.throwFeatMissing("tokenList", "jeqa.types.CoreNLPAnalysis");
    jcasType.jcas.checkArrayBounds(jcasType.ll_cas.ll_getRefValue(addr, ((CoreNLPAnalysis_Type)jcasType).casFeatCode_tokenList), i);
    return (Token)(jcasType.ll_cas.ll_getFSForRef(jcasType.ll_cas.ll_getRefArrayValue(jcasType.ll_cas.ll_getRefValue(addr, ((CoreNLPAnalysis_Type)jcasType).casFeatCode_tokenList), i)));}

  /** indexed setter for tokenList - sets an indexed value - 
   * @generated
   * @param i index in the array to set
   * @param v value to set into the array 
   */
  public void setTokenList(int i, Token v) { 
    if (CoreNLPAnalysis_Type.featOkTst && ((CoreNLPAnalysis_Type)jcasType).casFeat_tokenList == null)
      jcasType.jcas.throwFeatMissing("tokenList", "jeqa.types.CoreNLPAnalysis");
    jcasType.jcas.checkArrayBounds(jcasType.ll_cas.ll_getRefValue(addr, ((CoreNLPAnalysis_Type)jcasType).casFeatCode_tokenList), i);
    jcasType.ll_cas.ll_setRefArrayValue(jcasType.ll_cas.ll_getRefValue(addr, ((CoreNLPAnalysis_Type)jcasType).casFeatCode_tokenList), i, jcasType.ll_cas.ll_getFSRef(v));}
   
    
  //*--------------*
  //* Feature: partOfSpeechList

  /** getter for partOfSpeechList - gets 
   * @generated
   * @return value of the feature 
   */
  public FSArray getPartOfSpeechList() {
    if (CoreNLPAnalysis_Type.featOkTst && ((CoreNLPAnalysis_Type)jcasType).casFeat_partOfSpeechList == null)
      jcasType.jcas.throwFeatMissing("partOfSpeechList", "jeqa.types.CoreNLPAnalysis");
    return (FSArray)(jcasType.ll_cas.ll_getFSForRef(jcasType.ll_cas.ll_getRefValue(addr, ((CoreNLPAnalysis_Type)jcasType).casFeatCode_partOfSpeechList)));}
    
  /** setter for partOfSpeechList - sets  
   * @generated
   * @param v value to set into the feature 
   */
  public void setPartOfSpeechList(FSArray v) {
    if (CoreNLPAnalysis_Type.featOkTst && ((CoreNLPAnalysis_Type)jcasType).casFeat_partOfSpeechList == null)
      jcasType.jcas.throwFeatMissing("partOfSpeechList", "jeqa.types.CoreNLPAnalysis");
    jcasType.ll_cas.ll_setRefValue(addr, ((CoreNLPAnalysis_Type)jcasType).casFeatCode_partOfSpeechList, jcasType.ll_cas.ll_getFSRef(v));}    
    
  /** indexed getter for partOfSpeechList - gets an indexed value - 
   * @generated
   * @param i index in the array to get
   * @return value of the element at index i 
   */
  public PartOfSpeech getPartOfSpeechList(int i) {
    if (CoreNLPAnalysis_Type.featOkTst && ((CoreNLPAnalysis_Type)jcasType).casFeat_partOfSpeechList == null)
      jcasType.jcas.throwFeatMissing("partOfSpeechList", "jeqa.types.CoreNLPAnalysis");
    jcasType.jcas.checkArrayBounds(jcasType.ll_cas.ll_getRefValue(addr, ((CoreNLPAnalysis_Type)jcasType).casFeatCode_partOfSpeechList), i);
    return (PartOfSpeech)(jcasType.ll_cas.ll_getFSForRef(jcasType.ll_cas.ll_getRefArrayValue(jcasType.ll_cas.ll_getRefValue(addr, ((CoreNLPAnalysis_Type)jcasType).casFeatCode_partOfSpeechList), i)));}

  /** indexed setter for partOfSpeechList - sets an indexed value - 
   * @generated
   * @param i index in the array to set
   * @param v value to set into the array 
   */
  public void setPartOfSpeechList(int i, PartOfSpeech v) { 
    if (CoreNLPAnalysis_Type.featOkTst && ((CoreNLPAnalysis_Type)jcasType).casFeat_partOfSpeechList == null)
      jcasType.jcas.throwFeatMissing("partOfSpeechList", "jeqa.types.CoreNLPAnalysis");
    jcasType.jcas.checkArrayBounds(jcasType.ll_cas.ll_getRefValue(addr, ((CoreNLPAnalysis_Type)jcasType).casFeatCode_partOfSpeechList), i);
    jcasType.ll_cas.ll_setRefArrayValue(jcasType.ll_cas.ll_getRefValue(addr, ((CoreNLPAnalysis_Type)jcasType).casFeatCode_partOfSpeechList), i, jcasType.ll_cas.ll_getFSRef(v));}
   
    
  //*--------------*
  //* Feature: lemmaList

  /** getter for lemmaList - gets 
   * @generated
   * @return value of the feature 
   */
  public FSArray getLemmaList() {
    if (CoreNLPAnalysis_Type.featOkTst && ((CoreNLPAnalysis_Type)jcasType).casFeat_lemmaList == null)
      jcasType.jcas.throwFeatMissing("lemmaList", "jeqa.types.CoreNLPAnalysis");
    return (FSArray)(jcasType.ll_cas.ll_getFSForRef(jcasType.ll_cas.ll_getRefValue(addr, ((CoreNLPAnalysis_Type)jcasType).casFeatCode_lemmaList)));}
    
  /** setter for lemmaList - sets  
   * @generated
   * @param v value to set into the feature 
   */
  public void setLemmaList(FSArray v) {
    if (CoreNLPAnalysis_Type.featOkTst && ((CoreNLPAnalysis_Type)jcasType).casFeat_lemmaList == null)
      jcasType.jcas.throwFeatMissing("lemmaList", "jeqa.types.CoreNLPAnalysis");
    jcasType.ll_cas.ll_setRefValue(addr, ((CoreNLPAnalysis_Type)jcasType).casFeatCode_lemmaList, jcasType.ll_cas.ll_getFSRef(v));}    
    
  /** indexed getter for lemmaList - gets an indexed value - 
   * @generated
   * @param i index in the array to get
   * @return value of the element at index i 
   */
  public Lemma getLemmaList(int i) {
    if (CoreNLPAnalysis_Type.featOkTst && ((CoreNLPAnalysis_Type)jcasType).casFeat_lemmaList == null)
      jcasType.jcas.throwFeatMissing("lemmaList", "jeqa.types.CoreNLPAnalysis");
    jcasType.jcas.checkArrayBounds(jcasType.ll_cas.ll_getRefValue(addr, ((CoreNLPAnalysis_Type)jcasType).casFeatCode_lemmaList), i);
    return (Lemma)(jcasType.ll_cas.ll_getFSForRef(jcasType.ll_cas.ll_getRefArrayValue(jcasType.ll_cas.ll_getRefValue(addr, ((CoreNLPAnalysis_Type)jcasType).casFeatCode_lemmaList), i)));}

  /** indexed setter for lemmaList - sets an indexed value - 
   * @generated
   * @param i index in the array to set
   * @param v value to set into the array 
   */
  public void setLemmaList(int i, Lemma v) { 
    if (CoreNLPAnalysis_Type.featOkTst && ((CoreNLPAnalysis_Type)jcasType).casFeat_lemmaList == null)
      jcasType.jcas.throwFeatMissing("lemmaList", "jeqa.types.CoreNLPAnalysis");
    jcasType.jcas.checkArrayBounds(jcasType.ll_cas.ll_getRefValue(addr, ((CoreNLPAnalysis_Type)jcasType).casFeatCode_lemmaList), i);
    jcasType.ll_cas.ll_setRefArrayValue(jcasType.ll_cas.ll_getRefValue(addr, ((CoreNLPAnalysis_Type)jcasType).casFeatCode_lemmaList), i, jcasType.ll_cas.ll_getFSRef(v));}
   
    
  //*--------------*
  //* Feature: stemmedWordList

  /** getter for stemmedWordList - gets 
   * @generated
   * @return value of the feature 
   */
  public FSArray getStemmedWordList() {
    if (CoreNLPAnalysis_Type.featOkTst && ((CoreNLPAnalysis_Type)jcasType).casFeat_stemmedWordList == null)
      jcasType.jcas.throwFeatMissing("stemmedWordList", "jeqa.types.CoreNLPAnalysis");
    return (FSArray)(jcasType.ll_cas.ll_getFSForRef(jcasType.ll_cas.ll_getRefValue(addr, ((CoreNLPAnalysis_Type)jcasType).casFeatCode_stemmedWordList)));}
    
  /** setter for stemmedWordList - sets  
   * @generated
   * @param v value to set into the feature 
   */
  public void setStemmedWordList(FSArray v) {
    if (CoreNLPAnalysis_Type.featOkTst && ((CoreNLPAnalysis_Type)jcasType).casFeat_stemmedWordList == null)
      jcasType.jcas.throwFeatMissing("stemmedWordList", "jeqa.types.CoreNLPAnalysis");
    jcasType.ll_cas.ll_setRefValue(addr, ((CoreNLPAnalysis_Type)jcasType).casFeatCode_stemmedWordList, jcasType.ll_cas.ll_getFSRef(v));}    
    
  /** indexed getter for stemmedWordList - gets an indexed value - 
   * @generated
   * @param i index in the array to get
   * @return value of the element at index i 
   */
  public StemmedWord getStemmedWordList(int i) {
    if (CoreNLPAnalysis_Type.featOkTst && ((CoreNLPAnalysis_Type)jcasType).casFeat_stemmedWordList == null)
      jcasType.jcas.throwFeatMissing("stemmedWordList", "jeqa.types.CoreNLPAnalysis");
    jcasType.jcas.checkArrayBounds(jcasType.ll_cas.ll_getRefValue(addr, ((CoreNLPAnalysis_Type)jcasType).casFeatCode_stemmedWordList), i);
    return (StemmedWord)(jcasType.ll_cas.ll_getFSForRef(jcasType.ll_cas.ll_getRefArrayValue(jcasType.ll_cas.ll_getRefValue(addr, ((CoreNLPAnalysis_Type)jcasType).casFeatCode_stemmedWordList), i)));}

  /** indexed setter for stemmedWordList - sets an indexed value - 
   * @generated
   * @param i index in the array to set
   * @param v value to set into the array 
   */
  public void setStemmedWordList(int i, StemmedWord v) { 
    if (CoreNLPAnalysis_Type.featOkTst && ((CoreNLPAnalysis_Type)jcasType).casFeat_stemmedWordList == null)
      jcasType.jcas.throwFeatMissing("stemmedWordList", "jeqa.types.CoreNLPAnalysis");
    jcasType.jcas.checkArrayBounds(jcasType.ll_cas.ll_getRefValue(addr, ((CoreNLPAnalysis_Type)jcasType).casFeatCode_stemmedWordList), i);
    jcasType.ll_cas.ll_setRefArrayValue(jcasType.ll_cas.ll_getRefValue(addr, ((CoreNLPAnalysis_Type)jcasType).casFeatCode_stemmedWordList), i, jcasType.ll_cas.ll_getFSRef(v));}
  }

    