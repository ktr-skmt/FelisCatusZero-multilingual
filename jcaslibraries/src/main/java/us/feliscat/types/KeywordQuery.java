

/* First created by JCasGen Fri Mar 24 05:05:42 JST 2017 */
package us.feliscat.types;

import org.apache.uima.jcas.JCas; 
import org.apache.uima.jcas.JCasRegistry;
import org.apache.uima.jcas.cas.TOP_Type;



/** Single Keyword type Query
 * Updated by JCasGen Fri Mar 24 05:05:42 JST 2017
 * XML source: ../src/main/resources/desc/ts/typeSystem.xml
 * @generated */
public class KeywordQuery extends Query {
  /** @generated
   * @ordered 
   */
  @SuppressWarnings ("hiding")
  public final static int typeIndexID = JCasRegistry.register(KeywordQuery.class);
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
  protected KeywordQuery() {/* intentionally empty block */}
    
  /** Internal - constructor used by generator 
   * @generated
   * @param addr low level Feature Structure reference
   * @param type the type of this Feature Structure 
   */
  public KeywordQuery(int addr, TOP_Type type) {
    super(addr, type);
    readObject();
  }
  
  /** @generated
   * @param jcas JCas to which this Feature Structure belongs 
   */
  public KeywordQuery(JCas jcas) {
    super(jcas);
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
  //* Feature: keyword

  /** getter for keyword - gets keyword
   * @generated
   * @return value of the feature 
   */
  public Keyword getKeyword() {
    if (KeywordQuery_Type.featOkTst && ((KeywordQuery_Type)jcasType).casFeat_keyword == null)
      jcasType.jcas.throwFeatMissing("keyword", "us.feliscat.types.KeywordQuery");
    return (Keyword)(jcasType.ll_cas.ll_getFSForRef(jcasType.ll_cas.ll_getRefValue(addr, ((KeywordQuery_Type)jcasType).casFeatCode_keyword)));}
    
  /** setter for keyword - sets keyword 
   * @generated
   * @param v value to set into the feature 
   */
  public void setKeyword(Keyword v) {
    if (KeywordQuery_Type.featOkTst && ((KeywordQuery_Type)jcasType).casFeat_keyword == null)
      jcasType.jcas.throwFeatMissing("keyword", "us.feliscat.types.KeywordQuery");
    jcasType.ll_cas.ll_setRefValue(addr, ((KeywordQuery_Type)jcasType).casFeatCode_keyword, jcasType.ll_cas.ll_getFSRef(v));}    
  }

    