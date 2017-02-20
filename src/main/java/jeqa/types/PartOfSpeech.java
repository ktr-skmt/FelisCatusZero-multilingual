

/* First created by JCasGen Mon Feb 20 23:16:26 JST 2017 */
package jeqa.types;

import org.apache.uima.jcas.JCas; 
import org.apache.uima.jcas.JCasRegistry;
import org.apache.uima.jcas.cas.TOP_Type;

import org.apache.uima.jcas.cas.TOP;


/** 
 * Updated by JCasGen Mon Feb 20 23:16:26 JST 2017
 * XML source: src/main/resources/desc/ts/typeSystem.xml
 * @generated */
public class PartOfSpeech extends TOP {
  /** @generated
   * @ordered 
   */
  @SuppressWarnings ("hiding")
  public final static int typeIndexID = JCasRegistry.register(PartOfSpeech.class);
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
  protected PartOfSpeech() {/* intentionally empty block */}
    
  /** Internal - constructor used by generator 
   * @generated
   * @param addr low level Feature Structure reference
   * @param type the type of this Feature Structure 
   */
  public PartOfSpeech(int addr, TOP_Type type) {
    super(addr, type);
    readObject();
  }
  
  /** @generated
   * @param jcas JCas to which this Feature Structure belongs 
   */
  public PartOfSpeech(JCas jcas) {
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
  //* Feature: text

  /** getter for text - gets 
   * @generated
   * @return value of the feature 
   */
  public String getText() {
    if (PartOfSpeech_Type.featOkTst && ((PartOfSpeech_Type)jcasType).casFeat_text == null)
      jcasType.jcas.throwFeatMissing("text", "jeqa.types.PartOfSpeech");
    return jcasType.ll_cas.ll_getStringValue(addr, ((PartOfSpeech_Type)jcasType).casFeatCode_text);}
    
  /** setter for text - sets  
   * @generated
   * @param v value to set into the feature 
   */
  public void setText(String v) {
    if (PartOfSpeech_Type.featOkTst && ((PartOfSpeech_Type)jcasType).casFeat_text == null)
      jcasType.jcas.throwFeatMissing("text", "jeqa.types.PartOfSpeech");
    jcasType.ll_cas.ll_setStringValue(addr, ((PartOfSpeech_Type)jcasType).casFeatCode_text, v);}    
  }

    