/**
*******************************************************************************
* Copyright (C) 1996-2001, International Business Machines Corporation and    *
* others. All Rights Reserved.                                                *
*******************************************************************************
*
* $Source: /xsrl/Nsvn/icu/icu4jni/src/classes/com/ibm/icu4jni/test/converters/Attic/TestConv.java,v $ 
* $Date: 2001/03/24 02:59:10 $ 
* $Revision: 1.4 $
*
*******************************************************************************
*/ 

package com.ibm.icu4jni.test.converters;

import java.io.*;
import java.util.*;
import sun.io.*;
import com.ibm.icu4jni.converters.*;
public class TestConv{
    
    public static void main(String[] args){
           TestConv gbConvTest = new TestConv();
           gbConvTest.testISO2022JP();
           gbConvTest.testISO2022CN();
           gbConvTest.testShiftJis();
           gbConvTest.testLatin5();
      }
    final int REPEAT = 100000;
    final int BUFSIZE = 5;
    private void testFromUnicode(CharToByteConverter  gbConv,char[] uSource, char[] gbSource){

        try{
            byte[] myTarget = new byte[uSource.length * 5]; 
	        gbConv.convert(uSource, 0, uSource.length, myTarget, 0, myTarget.length);
            gbConv.reset();
            long start = System.currentTimeMillis();
            for(int i=0; i<=REPEAT;i++){
                gbConv.convert(uSource, 0, uSource.length, myTarget, 0, myTarget.length);
                gbConv.reset();
            }
            long stop = System.currentTimeMillis();
            System.out.println("time taken for "+ REPEAT +" iterations (CharToByte) (in ms): "+ (stop-start));
            char[] expectedResult = gbSource;
            boolean passed =true;
            int temp = gbConv.nextByteIndex();       
           /* while(i<=temp){
                System.out.print("0x"+Integer.toHexString((char)myTarget[i])+ ", ");
                i++;
            }*/
            System.out.println();
        /*    if((char)myTarget[i]!=expectedResult[i]){
                    StringBuffer temp = new StringBuffer("Error:  Expected: ");
                    temp.append("0x"+Integer.toHexString(expectedResult[i]));
                    temp.append(" Got: ");
                    temp.append("0x"+Integer.toHexString(myTarget[i]));
                    System.out.println(temp);
                    passed =false;
                }
                i++;
            }
            if(passed){
                System.out.println("--Test Unicode to GB18030 --PASSED");
            }else{
                System.out.println("--Test Unicode to GB18030 --FAILED");
            }
            */
                    
        }
        catch (MalformedInputException miEx){
            System.out.println(miEx.toString());
            if(gbConv!=null){
                System.out.println("Next char index"+gbConv.nextCharIndex());
            }
        }
        catch (UnknownCharacterException ucEx){
            System.out.println(ucEx.toString());
            if(gbConv!=null){
                System.out.println("Next char index"+gbConv.nextCharIndex());
            }

        }
        catch (ConversionBufferFullException cbfEx){
            System.out.println(cbfEx.toString());
        }    
                
    }
    private void testToUnicode(ByteToCharConverter  gbConv,char[] uSource, char[] gbSource){
        try{
            char[] myTarget = new char[gbSource.length];
            byte[] mySource = new byte[gbSource.length];
            int i=0;
            while(i<gbSource.length){
                mySource[i]=(byte) gbSource[i];
                i++;
            }

	        gbConv.convert(mySource, 0, mySource.length, myTarget, 0, myTarget.length);
            gbConv.reset();
            long start = System.currentTimeMillis();
            for(i=0; i<=REPEAT;i++){
                gbConv.convert(mySource, 0,mySource.length, myTarget, 0, myTarget.length);
                gbConv.reset();
            }
            long stop = System.currentTimeMillis();
            System.out.println("time taken for "+ REPEAT +" iterations (ByteToChar) (in ms): "+ (stop-start));
            i=0;
            char[] expectedResult = uSource;
            boolean passed =true;
        /* while(myTarget[i]!='\0'){
            if((char)myTarget[i]!=expectedResult[i]){
                    StringBuffer temp = new StringBuffer("Error:  Expected: ");
                    temp.append(expectedResult[i]);
                    temp.append(" Got: ");
                    temp.append(myTarget[i]);
                    System.out.println(temp);
                    passed =false;
                }
                i++;
            }
            if(passed){
                System.out.println("--Test Unicode to  is-2022-jp --PASSED");
            }else{
                System.out.println("--Test Unicode to is-2022-jp --FAILED");
            }*/
                    
        }
        catch (MalformedInputException miEx){
            System.out.println(miEx.toString());
            System.out.println("Next byte index : " + gbConv.nextByteIndex()+" which is :" +Integer.toHexString( gbSource[gbConv.nextByteIndex()]));
        }
        catch (UnknownCharacterException ucEx){
            System.out.println(ucEx.toString());
        }
        catch (ConversionBufferFullException cbfEx){
            System.out.println(cbfEx.toString());
        } 
                
    }

    public void testSmBufFromUnicode(CharToByteConverter gbConv, char[] uSource,char[] gbSource){
                
                
        char[] myUSource = uSource;
        char[] myGBSource =gbSource;
          
        gbConv.reset();
        byte[] myByteTarget = new byte[myUSource.length*5];
         char[] myCharTarget = new char[myUSource.length+1];
        long start = System.currentTimeMillis();
        for(int j=0; j<=REPEAT;j++){
            gbConv.reset();
            int inStart = 0;
            int inStop = myUSource.length;
            int outStart = 0;
            int outStop = 0;
            int i= BUFSIZE;
                            
            while(true){
                try{
                    outStop= (outStop+i>myByteTarget.length)? myByteTarget.length : (outStop+i) ;
                    inStop = (inStop+i>myUSource.length)? myUSource.length: (inStop+i);
                    inStart = gbConv.nextCharIndex();
                    outStart = gbConv.nextByteIndex();
                    gbConv.convert(myUSource, inStart, inStop,myByteTarget, outStart, outStop);
                    if(gbConv.nextCharIndex()>=myUSource.length){
                        break;
                    }
                }
                catch(ConversionBufferFullException cbEx){
                    continue;
                }
                catch (MalformedInputException miEx){
                    System.out.println(miEx.toString());
                    return;
                }
                catch (UnknownCharacterException ucEx){
                    System.out.println(ucEx.toString());
                    System.out.println(gbConv.nextCharIndex());
                    return;
                }
            }
        }
                      
        long stop = System.currentTimeMillis();
        System.out.println("time taken for "+ REPEAT +" iterations testSmBufFromUnicode (in ms): "+ (stop-start));
                        
    }
    public void testSmBufToUnicode(ByteToCharConverter  gbConv,char[] uSource, char[] gbSource){
                
                
        char[] myUSource = uSource;
        char[] myGBSource = gbSource;
        byte[] mygbSource = new byte[myGBSource.length];
        int j=0;
        while(j<myGBSource.length){
                mygbSource[j]=(byte) myGBSource[j];
                j++;
        }
        gbConv.reset();
        char[] myCharTarget = new char[myUSource.length+1];
        long start = System.currentTimeMillis();
        for( j=0; j<=REPEAT;j++){
            gbConv.reset();
            int inStart = 0;
            int inStop = myGBSource.length;
            int outStart = 0;
            int outStop = 0;
            int i=BUFSIZE;
            while(true){
                try{
                    outStop= (outStop+i>myCharTarget.length)? myCharTarget.length : (outStop+i) ;
                    inStop  = (inStop+i>myGBSource.length)? myGBSource.length : (inStop+i);
                    inStart = gbConv.nextByteIndex();
                    outStart = gbConv.nextCharIndex();
                    gbConv.convert(mygbSource, inStart, inStop, myCharTarget, outStart, outStop);
                    if(inStart>=mygbSource.length){
                        break;
                    }
                }
                catch(ConversionBufferFullException cbEx){
                    continue;
                }
                catch (MalformedInputException miEx){
                    System.out.println(miEx.toString());
                    return;
                }
                catch (UnknownCharacterException ucEx){
                    System.out.println(ucEx.toString());
                    return;
                }
            }
        }
    long stop = System.currentTimeMillis();
    System.out.println("time taken for "+ REPEAT +" iterations testSmBufToUnicode   (in ms): "+ (stop-start));
    }  
    public void testEUCJP(){
        System.out.println("EUCJP");

    }
    public void testLatin5(){
        System.out.println("LATIN5");
        char[] uSource={
                        0x00CC, 0x00CD, 0x00CE, 0x00CF, 0x00D1, 0x00D2, 0x00D3, 0x00D4, 0x00D5, 0x00D6,
                        0x00D7, 0x00D8, 0x00D9, 0x00DA, 0x00DB, 0x00DC, 0x00DF, 0x00E0, 0x00E1, 0x00E2,
                        0x00E3, 0x00E4, 0x00E5, 0x00E6, 0x00E7, 0x00E8, 0x00E9, 0x00EA, 0x00EB, 0x00EC,
                        0x00ED, 0x00EE, 0x00EF, 0x00F1, 0x00F2, 0x00F3, 0x00F4, 0x00F5, 0x00F6, 0x00F7,
                        0x00F8, 0x00F9, 0x00FA, 0x00FB, 0x00FC, 0x00FF, 0x011E, 0x011F, 0x0130, 0x0131,
                        0x015E, 0x015F 
                        };
       char[] gbSource ={ 
                        0xcc, 0xcd, 0xce, 0xcf, 0xd1, 0xd2, 0xd3, 0xd4, 0xd5, 0xd6, 
                        0xd7, 0xd8, 0xd9, 0xda, 0xdb, 0xdc, 0xdf, 0xe0, 0xe1, 0xe2, 
                        0xe3, 0xe4, 0xe5, 0xe6, 0xe7, 0xe8, 0xe9, 0xea, 0xeb, 0xec, 
                        0xed, 0xee, 0xef, 0xf1, 0xf2, 0xf3, 0xf4, 0xf5, 0xf6, 0xf7, 
                        0xf8, 0xf9, 0xfa, 0xfb, 0xfc, 0xff, 0xd0, 0xf0, 0xdd, 0xfd, 
                        0xde, 0xfe                    
                        };
        try{
            CharToByteConverter fromUConv =  CharToByteConverterICU.createConverter("latin5");
            ByteToCharConverter toUConv = ByteToCharConverterICU.createConverter("latin5");
            this.testToUnicode(toUConv, uSource,gbSource);
            this.testFromUnicode(fromUConv, uSource,gbSource);
            //this.testSmBufFromUnicode(fromUConv, uSource,gbSource);
            //this.testSmBufToUnicode(toUConv,uSource,gbSource);
        }
        catch(UnsupportedEncodingException ueEx){
            System.out.println(ueEx.toString());
        } 
    }
    public void testShiftJis(){
        System.out.println("ShiftJis");
        char[] uSource={
                        0xFF9A, 0xFF9B, 0xFF9C, 0xFF9D, 0xFF9E, 0xFF9F, 0xFFE0, 0xFFE1, 0xFFE2, 0xFFE2,
                        0xFFE2, 0xFFE3, 0xFF7B, 0xFFE5, 0xFF8C, 0xFF8D, 0xFF79, 0xFF7A, 0xFF7B, 0xFF7C,
                        0xFF7D, 0xFF7E, 0xFF7F, 0xFF80, 0xFF81, 0xFF82, 0xFF83, 0xFF84, 0xFF85, 0xFF54,
                        0xFF55, 0xFF56, 0xFF57, 0xFF58, 0xFF59, 0xFF5A, 0xFF5B, 0xFF5C, 0xFF5A, 0xFF5E,
                        0xFF61, 0xFF62, 0xFF63, 0xFF64, 0xFF65, 0xFF66, 0xFF67, 0xFF68, 0xFF69, 0xFF6A,
                        0xFF6B, 0xFF6C, 0xFF6D, 0xFF6E, 0xFF6F, 0xFF70, 0xFF71, 0xFF72, 0xFF73, 0xFF74,
                        0xFF75, 0xFF76, 0xFF77  
                        };
       char[] gbSource ={ 
                  
                        0xda, 0xdb, 0xdc, 0xdd, 0xde, 0xdf, 0x81, 0x91, 0x81, 0x92, 
                        0x81, 0xca, 0x81, 0xca, 0x81, 0xca, 0x81, 0x50, 0xbb, 0x81, 
                        0x8f, 0xcc, 0xcd, 0xb9, 0xba, 0xbb, 0xbc, 0xbd, 0xbe, 0xbf, 
                        0xc0, 0xc1, 0xc2, 0xc3, 0xc4, 0xc5, 0x82, 0x94, 0x82, 0x95, 
                        0x82, 0x96, 0x82, 0x97, 0x82, 0x98, 0x82, 0x99, 0x82, 0x9a, 
                        0x81, 0x6f, 0x81, 0x62, 0x81, 0x70, 0x82, 0x94, 0xa1, 0xa2, 
                        0xa3, 0xa4, 0xa5, 0xa6, 0xa7, 0xa8, 0xa9, 0xaa, 0xab, 0xac, 
                        0xad, 0xae, 0xaf, 0xb0, 0xb1, 0xb2, 0xb3, 0xb4, 0xb5, 0xb6, 
                        0xb7,                       
                        };
        try{
            CharToByteConverter fromUConv =  CharToByteConverterICU.createConverter("shift-jis");
            ByteToCharConverter toUConv = ByteToCharConverterICU.createConverter("shift-jis");
            this.testToUnicode(toUConv, uSource,gbSource);
            this.testFromUnicode(fromUConv, uSource,gbSource);
            this.testSmBufFromUnicode(fromUConv, uSource,gbSource);
            this.testSmBufToUnicode(toUConv,uSource,gbSource);
        }
        catch(UnsupportedEncodingException ueEx){
            System.out.println(ueEx.toString());
        } 
    }
    
    public void testISO2022JP() {
        System.out.println("ISO2022JP");
        
 char gbSource[]={        
                        0x1B,0x24,0x42,0x21,0x21,0x21,0x22,0x21,0x23,0x1B,0x28,0x42,0x20,0x1B,0x24,0x42,0x21,0x25,0x21,0x26,
                        0x21,0x27,0x21,0x28,0x1B,0x28,0x42,0x0D,0x0A,0x43,0x44,0x45,0x46,0x47,0x48,0x49,0x4A,0x0D,0x0A,0x1B,
                        0x24,0x42,0x21,0x29,0x21,0x2A,0x21,0x2B,0x21,0x2C,0x21,0x2D,0x21,0x2E,0x21,0x2F,0x21,0x30,0x1B,0x28,
                        0x42,0x0D,0x0A,0x4B,0x4C,0x4D,0x4E,0x4F,0x50,0x51,0x52,0x0D,0x0A,0x1B,0x24,0x42,0x21,0x31,0x21,0x32,
                        0x21,0x33,0x21,0x34,0x21,0x35,0x21,0x36,0x21,0x37,0x21,0x38,0x1B,0x28,0x42,0x0D,0x0A,0x1B,0x24,0x42,
                        0x21,0x39,0x21,0x3A,0x21,0x3B,0x21,0x3C,0x21,0x3D,0x21,0x3E,0x21,0x3F,0x1B,0x28,0x42,0x5C,0x0D,0x0A,
                        0x1B,0x24,0x42,0x21,0x43,0x21,0x44,0x21,0x45,0x21,0x46,0x21,0x47,0x21,0x48,0x1B,0x28,0x42,0x0D,0x0A,
                        0x1B,0x24,0x42,0x21,0x49,0x21,0x4A,0x21,0x4B,0x21,0x4C,0x25,0x2A,0x25,0x2B,0x25,0x2D,0x25,0x2F,0x1B,
                        0x28,0x42,0x0D,0x0A,0x1B,0x24,0x42,0x25,0x31,0x25,0x33,0x25,0x35,0x25,0x37,0x25,0x39,0x25,0x3B,0x25,
                        0x3D,0x25,0x3F,0x1B,0x28,0x42,0x0D,0x0A,0x53,0x54,0x55,0x56,0x57,0x58,0x59,0x5A,0x0D,0x0A,0x1B,0x24,
                        0x42,0x25,0x41,0x25,0x44,0x25,0x46,0x25,0x48,0x25,0x4A,0x25,0x4B,0x25,0x4C,0x25,0x4D,0x1B,0x28,0x42,
                        0x0D,0x0A,0x53,0x54,0x55,0x56,0x57,0x58,0x59,0x5A,0x0D,0x0A,0x1B,0x24,0x42,0x25,0x4E,0x25,0x4F,0x25,
                        0x52,0x1B,0x28,0x42,0x20,0x1B,0x24,0x42,0x25,0x58,0x25,0x5B,0x25,0x5E,0x25,0x5F,0x1B,0x28,0x42,0x0D,
                        0x0A,0x1B,0x24,0x42,0x25,0x60,0x25,0x61,0x25,0x62,0x25,0x64,0x25,0x66,0x25,0x68,0x25,0x69,0x25,0x6A,
                        0x1B,0x28,0x42,0x0D,0x0A,
                        /**/
                        0x1B,0x24,0x42,0x21,0x21,0x21,0x22,0x21,0x23,0x1B,0x28,0x42,0x20,0x1B,0x24,0x42,0x21,0x25,0x21,0x26,
                        0x21,0x27,0x21,0x28,0x1B,0x28,0x42,0x0D,0x0A,0x43,0x44,0x45,0x46,0x47,0x48,0x49,0x4A,0x0D,0x0A,0x1B,
                        0x24,0x42,0x21,0x29,0x21,0x2A,0x21,0x2B,0x21,0x2C,0x21,0x2D,0x21,0x2E,0x21,0x2F,0x21,0x30,0x1B,0x28,
                        0x42,0x0D,0x0A,0x4B,0x4C,0x4D,0x4E,0x4F,0x50,0x51,0x52,0x0D,0x0A,0x1B,0x24,0x42,0x21,0x31,0x21,0x32,
                        0x21,0x33,0x21,0x34,0x21,0x35,0x21,0x36,0x21,0x37,0x21,0x38,0x1B,0x28,0x42,0x0D,0x0A,0x1B,0x24,0x42,
                        0x21,0x39,0x21,0x3A,0x21,0x3B,0x21,0x3C,0x21,0x3D,0x21,0x3E,0x21,0x3F,0x1B,0x28,0x42,0x5C,0x0D,0x0A,
                        0x1B,0x24,0x42,0x21,0x43,0x21,0x44,0x21,0x45,0x21,0x46,0x21,0x47,0x21,0x48,0x1B,0x28,0x42,0x0D,0x0A,
                        0x1B,0x24,0x42,0x21,0x49,0x21,0x4A,0x21,0x4B,0x21,0x4C,0x25,0x2A,0x25,0x2B,0x25,0x2D,0x25,0x2F,0x1B,
                        0x28,0x42,0x0D,0x0A,0x1B,0x24,0x42,0x25,0x31,0x25,0x33,0x25,0x35,0x25,0x37,0x25,0x39,0x25,0x3B,0x25,
                        0x3D,0x25,0x3F,0x1B,0x28,0x42,0x0D,0x0A,0x53,0x54,0x55,0x56,0x57,0x58,0x59,0x5A,0x0D,0x0A,0x1B,0x24,
                        0x42,0x25,0x41,0x25,0x44,0x25,0x46,0x25,0x48,0x25,0x4A,0x25,0x4B,0x25,0x4C,0x25,0x4D,0x1B,0x28,0x42,
                        0x0D,0x0A,0x53,0x54,0x55,0x56,0x57,0x58,0x59,0x5A,0x0D,0x0A,0x1B,0x24,0x42,0x25,0x4E,0x25,0x4F,0x25,
                        0x52,0x1B,0x28,0x42,0x20,0x1B,0x24,0x42,0x25,0x58,0x25,0x5B,0x25,0x5E,0x25,0x5F,0x1B,0x28,0x42,0x0D,
                        0x0A,0x1B,0x24,0x42,0x25,0x60,0x25,0x61,0x25,0x62,0x25,0x64,0x25,0x66,0x25,0x68,0x25,0x69,0x25,0x6A,
                        0x1B,0x28,0x42,0x0D,0x0A,
                        /**/
                        0x1B,0x24,0x42,0x21,0x21,0x21,0x22,0x21,0x23,0x1B,0x28,0x42,0x20,0x1B,0x24,0x42,0x21,0x25,0x21,0x26,
                        0x21,0x27,0x21,0x28,0x1B,0x28,0x42,0x0D,0x0A,0x43,0x44,0x45,0x46,0x47,0x48,0x49,0x4A,0x0D,0x0A,0x1B,
                        0x24,0x42,0x21,0x29,0x21,0x2A,0x21,0x2B,0x21,0x2C,0x21,0x2D,0x21,0x2E,0x21,0x2F,0x21,0x30,0x1B,0x28,
                        0x42,0x0D,0x0A,0x4B,0x4C,0x4D,0x4E,0x4F,0x50,0x51,0x52,0x0D,0x0A,0x1B,0x24,0x42,0x21,0x31,0x21,0x32,
                        0x21,0x33,0x21,0x34,0x21,0x35,0x21,0x36,0x21,0x37,0x21,0x38,0x1B,0x28,0x42,0x0D,0x0A,0x1B,0x24,0x42,
                        0x21,0x39,0x21,0x3A,0x21,0x3B,0x21,0x3C,0x21,0x3D,0x21,0x3E,0x21,0x3F,0x1B,0x28,0x42,0x5C,0x0D,0x0A,
                        0x1B,0x24,0x42,0x21,0x43,0x21,0x44,0x21,0x45,0x21,0x46,0x21,0x47,0x21,0x48,0x1B,0x28,0x42,0x0D,0x0A,
                        0x1B,0x24,0x42,0x21,0x49,0x21,0x4A,0x21,0x4B,0x21,0x4C,0x25,0x2A,0x25,0x2B,0x25,0x2D,0x25,0x2F,0x1B,
                        0x28,0x42,0x0D,0x0A,0x1B,0x24,0x42,0x25,0x31,0x25,0x33,0x25,0x35,0x25,0x37,0x25,0x39,0x25,0x3B,0x25,
                        0x3D,0x25,0x3F,0x1B,0x28,0x42,0x0D,0x0A,0x53,0x54,0x55,0x56,0x57,0x58,0x59,0x5A,0x0D,0x0A,0x1B,0x24,
                        0x42,0x25,0x41,0x25,0x44,0x25,0x46,0x25,0x48,0x25,0x4A,0x25,0x4B,0x25,0x4C,0x25,0x4D,0x1B,0x28,0x42,
                        0x0D,0x0A,0x53,0x54,0x55,0x56,0x57,0x58,0x59,0x5A,0x0D,0x0A,0x1B,0x24,0x42,0x25,0x4E,0x25,0x4F,0x25,
                        0x52,0x1B,0x28,0x42,0x20,0x1B,0x24,0x42,0x25,0x58,0x25,0x5B,0x25,0x5E,0x25,0x5F,0x1B,0x28,0x42,0x0D,
                        0x0A,0x1B,0x24,0x42,0x25,0x60,0x25,0x61,0x25,0x62,0x25,0x64,0x25,0x66,0x25,0x68,0x25,0x69,0x25,0x6A,
                        0x1B,0x28,0x42,0x0D,0x0A,
                        /**/
                        0x1B,0x24,0x42,0x21,0x21,0x21,0x22,0x21,0x23,0x1B,0x28,0x42,0x20,0x1B,0x24,0x42,0x21,0x25,0x21,0x26,
                        0x21,0x27,0x21,0x28,0x1B,0x28,0x42,0x0D,0x0A,0x43,0x44,0x45,0x46,0x47,0x48,0x49,0x4A,0x0D,0x0A,0x1B,
                        0x24,0x42,0x21,0x29,0x21,0x2A,0x21,0x2B,0x21,0x2C,0x21,0x2D,0x21,0x2E,0x21,0x2F,0x21,0x30,0x1B,0x28,
                        0x42,0x0D,0x0A,0x4B,0x4C,0x4D,0x4E,0x4F,0x50,0x51,0x52,0x0D,0x0A,0x1B,0x24,0x42,0x21,0x31,0x21,0x32,
                        0x21,0x33,0x21,0x34,0x21,0x35,0x21,0x36,0x21,0x37,0x21,0x38,0x1B,0x28,0x42,0x0D,0x0A,0x1B,0x24,0x42,
                        0x21,0x39,0x21,0x3A,0x21,0x3B,0x21,0x3C,0x21,0x3D,0x21,0x3E,0x21,0x3F,0x1B,0x28,0x42,0x5C,0x0D,0x0A,
                        0x1B,0x24,0x42,0x21,0x43,0x21,0x44,0x21,0x45,0x21,0x46,0x21,0x47,0x21,0x48,0x1B,0x28,0x42,0x0D,0x0A,
                        0x1B,0x24,0x42,0x21,0x49,0x21,0x4A,0x21,0x4B,0x21,0x4C,0x25,0x2A,0x25,0x2B,0x25,0x2D,0x25,0x2F,0x1B,
                        0x28,0x42,0x0D,0x0A,
                        0x1B,0x28,0x42,0x0D,0x0A,

                    };
    char uSource[]={        
                        0x3000, 0x3001, 0x3002, 0x0020, 0xFF0E, 0x30FB, 0xFF1A, 0xFF1B, 0x000D, 0x000A,
                        0x0043, 0x0044, 0x0045, 0x0046, 0x0047, 0x0048, 0x0049, 0x004A, 0x000D, 0x000A, 
                        0xFF1F, 0xFF01, 0x309B, 0x309C, 0x00B4, 0xFF40, 0x00A8, 0xFF3E, 0x000D, 0x000A,
                        0x004B, 0x004C, 0x004D, 0x004E, 0x004F, 0x0050, 0x0051, 0x0052, 0x000D, 0x000A,
                        0xFFE3, 0xFF3F, 0x30FD, 0x30FE, 0x309D, 0x309E, 0x3003, 0x4EDD, 0x000D, 0x000A, 
                        0x3005, 0x3006, 0x3007, 0x30FC, 0x2015, 0x2010, 0xFF0F, 0x005C, 0x000D, 0x000A, 
                        /*0x301C, 0x2016,*/ 0xFF5C, 0x2026, 0x2025, 0x2018, 0x2019, 0x201C, 0x000D, 0x000A, 
                        0x201D, 0xFF08, 0xFF09, 0x3014, 0xFF75, 0xFF76, 0xFF77, 0xFF78, 0x000D, 0x000A,
                        0xFF79, 0xFF7A, 0xFF7B, 0xFF7C, 0xFF7D, 0xFF7E, 0xFF7F, 0xFF80, 0x000D, 0x000A,
                        0x0053, 0x0054, 0x0055, 0x0056, 0x0057, 0x0058, 0x0059, 0x005A, 0x000D, 0x000A,
                        0xFF81, 0xFF82, 0xFF83, 0xFF84, 0xFF85, 0xFF86, 0xFF87, 0xFF88, 0x000D, 0x000A,
                        0x0053, 0x0054, 0x0055, 0x0056, 0x0057, 0x0058, 0x0059, 0x005A, 0x000D, 0x000A,
                        0xFF89, 0xFF8A, 0xFF8B, 0x0020, 0xFF8D, 0xFF8E, 0xFF8F, 0xFF90, 0x000D, 0x000A,
                        0xFF91, 0xFF92, 0xFF93, 0xFF94, 0xFF95, 0xFF96, 0xFF97, 0xFF98, 0x000D, 0x000A,
                        /**/
                        0x3000, 0x3001, 0x3002, 0x0020, 0xFF0E, 0x30FB, 0xFF1A, 0xFF1B, 0x000D, 0x000A,
                        0x0043, 0x0044, 0x0045, 0x0046, 0x0047, 0x0048, 0x0049, 0x004A, 0x000D, 0x000A, 
                        0xFF1F, 0xFF01, 0x309B, 0x309C, 0x00B4, 0xFF40, 0x00A8, 0xFF3E, 0x000D, 0x000A,
                        0x004B, 0x004C, 0x004D, 0x004E, 0x004F, 0x0050, 0x0051, 0x0052, 0x000D, 0x000A,
                        0xFFE3, 0xFF3F, 0x30FD, 0x30FE, 0x309D, 0x309E, 0x3003, 0x4EDD, 0x000D, 0x000A, 
                        0x3005, 0x3006, 0x3007, 0x30FC, 0x2015, 0x2010, 0xFF0F, 0x005C, 0x000D, 0x000A, 
                        /*0x301C, 0x2016,*/ 0xFF5C, 0x2026, 0x2025, 0x2018, 0x2019, 0x201C, 0x000D, 0x000A, 
                        0x201D, 0xFF08, 0xFF09, 0x3014, 0xFF75, 0xFF76, 0xFF77, 0xFF78, 0x000D, 0x000A,
                        0xFF79, 0xFF7A, 0xFF7B, 0xFF7C, 0xFF7D, 0xFF7E, 0xFF7F, 0xFF80, 0x000D, 0x000A,
                        0x0053, 0x0054, 0x0055, 0x0056, 0x0057, 0x0058, 0x0059, 0x005A, 0x000D, 0x000A,
                        0xFF81, 0xFF82, 0xFF83, 0xFF84, 0xFF85, 0xFF86, 0xFF87, 0xFF88, 0x000D, 0x000A,
                        0x0053, 0x0054, 0x0055, 0x0056, 0x0057, 0x0058, 0x0059, 0x005A, 0x000D, 0x000A,
                        0xFF89, 0xFF8A, 0xFF8B, 0x0020, 0xFF8D, 0xFF8E, 0xFF8F, 0xFF90, 0x000D, 0x000A,
                        0xFF91, 0xFF92, 0xFF93, 0xFF94, 0xFF95, 0xFF96, 0xFF97, 0xFF98, 0x000D, 0x000A,
                        /**/
                        0x3000, 0x3001, 0x3002, 0x0020, 0xFF0E, 0x30FB, 0xFF1A, 0xFF1B, 0x000D, 0x000A,
                        0x0043, 0x0044, 0x0045, 0x0046, 0x0047, 0x0048, 0x0049, 0x004A, 0x000D, 0x000A, 
                        0xFF1F, 0xFF01, 0x309B, 0x309C, 0x00B4, 0xFF40, 0x00A8, 0xFF3E, 0x000D, 0x000A,
                        0x004B, 0x004C, 0x004D, 0x004E, 0x004F, 0x0050, 0x0051, 0x0052, 0x000D, 0x000A,
                        0xFFE3, 0xFF3F, 0x30FD, 0x30FE, 0x309D, 0x309E, 0x3003, 0x4EDD, 0x000D, 0x000A, 
                        0x3005, 0x3006, 0x3007, 0x30FC, 0x2015, 0x2010, 0xFF0F, 0x005C, 0x000D, 0x000A, 
                        /*0x301C, 0x2016,*/ 0xFF5C, 0x2026, 0x2025, 0x2018, 0x2019, 0x201C, 0x000D, 0x000A, 
                        0x201D, 0xFF08, 0xFF09, 0x3014, 0xFF75, 0xFF76, 0xFF77, 0xFF78, 0x000D, 0x000A,
                        0xFF79, 0xFF7A, 0xFF7B, 0xFF7C, 0xFF7D, 0xFF7E, 0xFF7F, 0xFF80, 0x000D, 0x000A,
                        0x0053, 0x0054, 0x0055, 0x0056, 0x0057, 0x0058, 0x0059, 0x005A, 0x000D, 0x000A,
                        0xFF81, 0xFF82, 0xFF83, 0xFF84, 0xFF85, 0xFF86, 0xFF87, 0xFF88, 0x000D, 0x000A,
                        0x0053, 0x0054, 0x0055, 0x0056, 0x0057, 0x0058, 0x0059, 0x005A, 0x000D, 0x000A,
                        0xFF89, 0xFF8A, 0xFF8B, 0x0020, 0xFF8D, 0xFF8E, 0xFF8F, 0xFF90, 0x000D, 0x000A,
                        0xFF91, 0xFF92, 0xFF93, 0xFF94, 0xFF95, 0xFF96, 0xFF97, 0xFF98, 0x000D, 0x000A,                        
                        /**/
                        0x3000, 0x3001, 0x3002, 0x0020, 0xFF0E, 0x30FB, 0xFF1A, 0xFF1B, 0x000D, 0x000A,
                        0x0043, 0x0044, 0x0045, 0x0046, 0x0047, 0x0048, 0x0049, 0x004A, 0x000D, 0x000A, 
                        0xFF1F, 0xFF01, 0x309B, 0x309C, 0x00B4, 0xFF40, 0x00A8, 0xFF3E, 0x000D, 0x000A,
                        0x004B, 0x004C, 0x004D, 0x004E, 0x004F, 0x0050, 0x0051, 0x0052, 0x000D, 0x000A,
                        0xFFE3, 0xFF3F, 0x30FD, 0x30FE, 0x309D, 0x309E, 0x3003, 0x4EDD, 0x000D, 0x000A, 
                        0x3005, 0x3006, 0x3007, 0x30FC, 0x2015, 0x2010, 0xFF0F, 0x005C, 0x000D, 0x000A, 
                        /*0x301C, 0x2016,*/ 0xFF5C, 0x2026, 0x2025, 0x2018, 0x2019, 0x201C, 0x000D, 0x000A, 
                        0x201D, 0xFF08, 0xFF09, 0x3014, 0xFF75, 0xFF76, 0xFF77, 0xFF78, 0x000D, 0x000A,
                        0xFF79, 0xFF7A, 0xFF7B, 0xFF7C, 0xFF7D, 0xFF7E, 0xFF7F, 0xFF80, 0x000D, 0x000A,
                        0x0053, 0x0054, 0x0055, 0x0056, 0x0057, 0x0058, 0x0059, 0x005A, 0x000D, 0x000A,
                        0xFF81, 0xFF82, 0xFF83, 0xFF84, 0xFF85, 0xFF86, 0xFF87, 0xFF88, 0x000D, 0x000A,
                        0x0053, 0x0054, 0x0055, 0x0056, 0x0057, 0x0058, 0x0059, 0x005A, 0x000D, 0x000A,
                        0xFF89, 0xFF8A, 0xFF8B, 0x0020, 0xFF8D, 0xFF8E, 0xFF8F, 0xFF90, 0x000D, 0x000A,
                        0xFF91, 0xFF92, 0xFF93, 0xFF94, 0xFF95, 0xFF96, 0xFF97, 0xFF98, 0x000D, 0x000A,
                        /**/
                        0x3000, 0x3001, 0x3002, 0x0020, 0xFF0E, 0x30FB, 0xFF1A, 0xFF1B, 0x000D, 0x000A,
                        0x0043, 0x0044, 0x0045, 0x0046, 0x0047, 0x0048, 0x0049, 0x004A, 0x000D, 0x000A, 
                        0xFF1F, 0xFF01, 0x309B, 0x309C, 0x00B4, 0xFF40, 0x00A8, 0xFF3E, 0x000D, 0x000A,
                        0x004B, 0x004C, 0x004D, 0x004E, 0x004F, 0x0050, 0x0051, 0x0052, 0x000D, 0x000A,
                        0xFFE3, 0xFF3F, 0x30FD, 0x30FE, 0x309D, 0x309E, 0x3003, 0x4EDD, 0x000D, 0x000A, 
                        0x3005, 0x3006, 0x3007, 0x30FC, 0x2015, 0x2010, 0xFF0F, 0x005C, 0x000D, 0x000A, 
                        /*0x301C, 0x2016,*/ 0xFF5C, 0x2026, 0x2025, 0x2018, 0x2019, 0x201C, 0x000D, 0x000A, 
                        0x201D, 0xFF08, 0xFF09, 0x3014, 0xFF75, 0xFF76, 0xFF77, 0xFF78, 0x000D, 0x000A,
                        0xFF79, 0xFF7A, 0xFF7B, 0xFF7C, 0xFF7D, 0xFF7E, 0xFF7F, 0xFF80, 0x000D, 0x000A,
                        0x0053, 0x0054, 0x0055, 0x0056, 0x0057, 0x0058, 0x0059, 0x005A, 0x000D, 0x000A,
                        0xFF81, 0xFF82, 0xFF83, 0xFF84, 0xFF85, 0xFF86, 0xFF87, 0xFF88, 0x000D, 0x000A,
                        0x0053, 0x0054, 0x0055, 0x0056, 0x0057, 0x0058, 0x0059, 0x005A, 0x000D, 0x000A,
                        0xFF89, 0xFF8A, 0xFF8B, 0x0020, 0xFF8D, 0xFF8E, 0xFF8F, 0xFF90, 0x000D, 0x000A,
                        0xFF91, 0xFF92, 0xFF93, 0xFF94, 0xFF95, 0xFF96, 0xFF97, 0xFF98, 0x000D, 0x000A,
                                                /**/
                        0x3000, 0x3001, 0x3002, 0x0020, 0xFF0E, 0x30FB, 0xFF1A, 0xFF1B, 0x000D, 0x000A,
                        0x0043, 0x0044, 0x0045, 0x0046, 0x0047, 0x0048, 0x0049, 0x004A, 0x000D, 0x000A, 
                        0xFF1F, 0xFF01, 0x309B, 0x309C, 0x00B4, 0xFF40, 0x00A8, 0xFF3E, 0x000D, 0x000A,
                        0x004B, 0x004C, 0x004D, 0x004E, 0x004F, 0x0050, 0x0051, 0x0052, 0x000D, 0x000A,
                        0xFFE3, 0xFF3F, 0x30FD, 0x30FE, 0x309D, 0x309E, 0x3003, 0x4EDD, 0x000D, 0x000A, 
                        0x3005, 0x3006, 0x3007, 0x30FC, 0x2015, 0x2010, 0xFF0F, 0x005C, 0x000D, 0x000A, 
                        /*0x301C, 0x2016,*/ 0xFF5C, 0x2026, 0x2025, 0x2018, 0x2019, 0x201C, 0x000D, 0x000A, 
                        0x201D, 0xFF08, 0xFF09, 0x3014, 0xFF75, 0xFF76, 0xFF77, 0xFF78, 0x000D, 0x000A,
                        0xFF79, 0xFF7A, 0xFF7B, 0xFF7C, 0xFF7D, 0xFF7E, 0xFF7F, 0xFF80, 0x000D, 0x000A,
                        0x0053, 0x0054, 0x0055, 0x0056, 0x0057, 0x0058, 0x0059, 0x005A, 0x000D, 0x000A,
                        0xFF81, 0xFF82, 0xFF83, 0xFF84, 0xFF85, 0xFF86, 0xFF87, 0xFF88, 0x000D, 0x000A,
                        0x0053, 0x0054, 0x0055, 0x0056, 0x0057, 0x0058, 0x0059, 0x005A, 0x000D, 0x000A,
                        0xFF89, 0xFF8A, 0xFF8B, 0x0020, 0xFF8D, 0xFF8E, 0xFF8F, 0xFF90, 0x000D, 0x000A,
                        0xFF91, 0xFF92, 0xFF93, 0xFF94, 0xFF95, 0xFF96, 0xFF97, 0xFF98, 0x000D, 0x000A,
                                                /**/
                        0x3000, 0x3001, 0x3002, 0x0020, 0xFF0E, 0x30FB, 0xFF1A, 0xFF1B, 0x000D, 0x000A,
                        0x0043, 0x0044, 0x0045, 0x0046, 0x0047, 0x0048, 0x0049, 0x004A, 0x000D, 0x000A, 
                        0xFF1F, 0xFF01, 0x309B, 0x309C, 0x00B4, 0xFF40, 0x00A8, 0xFF3E, 0x000D, 0x000A,
                        0x004B, 0x004C, 0x004D, 0x004E, 0x004F, 0x0050, 0x0051, 0x0052, 0x000D, 0x000A,
                        0xFFE3, 0xFF3F, 0x30FD, 0x30FE, 0x309D, 0x309E, 0x3003, 0x4EDD, 0x000D, 0x000A, 
                        0x3005, 0x3006, 0x3007, 0x30FC, 0x2015, 0x2010, 0xFF0F, 0x005C, 0x000D, 0x000A, 
                        /*0x301C, 0x2016,*/ 0xFF5C, 0x2026, 0x2025, 0x2018, 0x2019, 0x201C, 0x000D, 0x000A, 
                        0x201D, 0xFF08, 0xFF09, 0x3014, 0xFF75, 0xFF76, 0xFF77, 0xFF78, 0x000D, 0x000A,
                        0xFF79, 0xFF7A, 0xFF7B, 0xFF7C, 0xFF7D, 0xFF7E, 0xFF7F, 0xFF80, 0x000D, 0x000A,
                        0x0053, 0x0054, 0x0055, 0x0056, 0x0057, 0x0058, 0x0059, 0x005A, 0x000D, 0x000A,
                        0xFF81, 0xFF82, 0xFF83, 0xFF84, 0xFF85, 0xFF86, 0xFF87, 0xFF88, 0x000D, 0x000A,
                        0x0053, 0x0054, 0x0055, 0x0056, 0x0057, 0x0058, 0x0059, 0x005A, 0x000D, 0x000A,
                        0xFF89, 0xFF8A, 0xFF8B, 0x0020, 0xFF8D, 0xFF8E, 0xFF8F, 0xFF90, 0x000D, 0x000A,
                        0xFF91, 0xFF92, 0xFF93, 0xFF94, 0xFF95, 0xFF96, 0xFF97, 0xFF98, 0x000D, 0x000A,
                                                /**/
                        0x3000, 0x3001, 0x3002, 0x0020, 0xFF0E, 0x30FB, 0xFF1A, 0xFF1B, 0x000D, 0x000A,
                        0x0043, 0x0044, 0x0045, 0x0046, 0x0047, 0x0048, 0x0049, 0x004A, 0x000D, 0x000A, 
                        0xFF1F, 0xFF01, 0x309B, 0x309C, 0x00B4, 0xFF40, 0x00A8, 0xFF3E, 0x000D, 0x000A,
                        0x004B, 0x004C, 0x004D, 0x004E, 0x004F, 0x0050, 0x0051, 0x0052, 0x000D, 0x000A,
                        0xFFE3, 0xFF3F, 0x30FD, 0x30FE, 0x309D, 0x309E, 0x3003, 0x4EDD, 0x000D, 0x000A, 
                        0x3005, 0x3006, 0x3007, 0x30FC, 0x2015, 0x2010, 0xFF0F, 0x005C,
                        };


        try{
            CharToByteConverter fromUConv =  CharToByteConverterICU.createConverter("iso-2022-jp");
            ByteToCharConverter toUConv = ByteToCharConverterICU.createConverter("iso-2022-jp");
            this.testToUnicode(toUConv, uSource,gbSource);
            this.testFromUnicode(fromUConv, uSource,gbSource);
            //this.testSmBufFromUnicode(fromUConv, uSource,gbSource);
            //this.testSmBufToUnicode(toUConv,uSource,gbSource);
        }
        catch(UnsupportedEncodingException ueEx){
            System.out.println(ueEx.toString());
        }
            
        
    }
    public void testISO2022CN() {
        System.out.println("ISO2022CN");

        char[] uSource = { 
                            0x4E00, 0x4E00, 0x4E01, 0x4E03, 0x60F6, 0x60F7, 0x60F8, 0x60FB, 0x000D, 0x000A,
                            0x0392, 0x0393, 0x0394, 0x0395, 0x0396, 0x0397, 0x60FB, 0x60FC, 0x000D, 0x000A,
                            0x4E07, 0x4E08, 0x4E08, 0x4E09, 0x4E0A, 0x4E0B, 0x0042, 0x0043, 0x000D, 0x000A,
                            0x4E0C, 0x0021, 0x0022, 0x0023, 0x0024, 0xFF40, 0xFF41, 0xFF42, 0x000D, 0x000A,
                            0xFF43, 0xFF44, 0xFF45, 0xFF46, 0xFF47, 0xFF48, 0xFF49, 0xFF4A, 0x000D, 0x000A,
                            0xFF4B, 0xFF4C, 0xFF4D, 0xFF4E, 0xFF4F, 0x6332, 0x63B0, 0x643F, 0x000D, 0x000A,
                            0x64D8, 0x8004, 0x6BEA, 0x6BF3, 0x6BFD, 0x6BF5, 0x6BF9, 0x6C05, 0x000D, 0x000A,
                            0x6C07, 0x6C06, 0x6C0D, 0x6C15, 0x9CD9, 0x9CDC, 0x9CDD, 0x9CDF, 0x000D, 0x000A,
                            0x9CE2, 0x977C, 0x9785, 0x9791, 0x9792, 0x9794, 0x97AF, 0x97AB, 0x000D, 0x000A,
                            0x97A3, 0x97B2, 0x97B4, 0x9AB1, 0x9AB0, 0x9AB7, 0x9E58, 0x9AB6, 0x000D, 0x000A,
                            0x9ABA, 0x9ABC, 0x9AC1, 0x9AC0, 0x9AC5, 0x9AC2, 0x9ACB, 0x9ACC, 0x000D, 0x000A,
                            0x9AD1, 0x9B45, 0x9B43, 0x9B47, 0x9B49, 0x9B48, 0x9B4D, 0x9B51, 0x000D, 0x000A,
                            0x98E8, 0x990D, 0x992E, 0x9955, 0x9954, 0x9ADF, 0x60FE, 0x60FF, 0x000D, 0x000A,
                            0x60F2, 0x60F3, 0x60F4, 0x60F5, 0x000D, 0x000A, 0x60F9, 0x60FA, 0x000D, 0x000A,
                            0x6100, 0x6101, 0x0041, 0x0044, 0x0045, 0x0046, 0x0047, 0x0048, 0x000D, 0x000A,
                            0x247D, 0x247E, 0x247F, 0x2480, 0x2481, 0x2482, 0x2483, 0x2484, 0x2485, 0x2486,
                            0x2487, 0x2460, 0x2461, 0xFF20, 0xFF21, 0xFF22, 0x0049, 0x004A, 0x000D, 0x000A,
        };
        char[] gbSource = {
                0x1b, 0x24, 0x29, 0x41, 0x0e, 0x52,  0x3b, 0x52, 0x3b, 0x36, 0x21, 0x46, 0x5f, 0x3b, 0x4c, 0x1b, 
                0x24, 0x2a, 0x48, 0x1b, 0x4f, 0x42, 0x6b, 0x1b, 0x4f, 0x3b, 0x69, 0x1b, 0x24, 0x29, 0x47, 0x1b, 
                0x4e, 0x5f, 0x33, 0x0d, 0x0a, 0x1b, 0x24, 0x29, 0x47, 0x1b, 0x4e, 0x24, 0x76, 0x1b, 0x4e, 0x24, 
                0x77, 0x1b, 0x4e, 0x24, 0x78, 0x1b, 0x4e, 0x24, 0x79, 0x1b, 0x4e, 0x24, 0x7a, 0x1b, 0x4e, 0x24, 
                0x7b, 0x1b, 0x4e, 0x5f, 0x33, 0x1b, 0x24, 0x2a, 0x48, 0x1b, 0x4f, 0x3b, 0x6a, 0x0d, 0x0a, 0x1b, 
                0x24, 0x2a, 0x48, 0x1b, 0x4f, 0x21, 0x26, 0x1b, 0x24, 0x29, 0x47, 0x1b, 0x4e, 0x44, 0x37, 0x1b, 
                0x4e, 0x44, 0x37, 0x1b, 0x4e, 0x44, 0x35, 0x1b, 0x4e, 0x44, 0x38, 0x1b, 0x4e, 0x44, 0x36, 0x1b, 
                0x4e, 0x24, 0x42, 0x1b, 0x4e, 0x24, 0x43, 0x0d, 0x0a, 0x1b, 0x24, 0x2a, 0x48, 0x1b, 0x4f, 0x21, 
                0x27, 0x1b, 0x24, 0x29, 0x47, 0x1b, 0x4e, 0x21, 0x2a, 0x0f, 0x22, 0x23, 0x24, 0x1b, 0x24, 0x29, 
                0x41, 0x0e, 0x23, 0x60, 0x23, 0x61, 0x23, 0x62, 0x0f, 0x0d, 0x0a, 0x1b, 0x24, 0x29, 0x41, 0x0e, 
                0x23, 0x63, 0x23, 0x64, 0x23, 0x65, 0x23, 0x66, 0x23, 0x67, 0x23, 0x68, 0x23, 0x69, 0x23, 0x6a, 
                0x0f, 0x0d, 0x0a, 0x1b, 0x24, 0x29, 0x41, 0x0e, 0x23, 0x6b, 0x23, 0x6c, 0x23, 0x6d, 0x23, 0x6e, 
                0x23, 0x6f, 0x6a, 0x7d, 0x6a, 0x7e, 0x6b, 0x21, 0x0f, 0x0d, 0x0a, 0x1b, 0x24, 0x29, 0x41, 0x0e, 
                0x6b, 0x22, 0x6b, 0x23, 0x6b, 0x24, 0x6b, 0x25, 0x6b, 0x26, 0x6b, 0x27, 0x6b, 0x28, 0x6b, 0x29, 
                0x0f, 0x0d, 0x0a, 0x1b, 0x24, 0x29, 0x41, 0x0e, 0x6b, 0x2a, 0x6b, 0x2b, 0x6b, 0x2c, 0x6b, 0x2d, 
                0x77, 0x2b, 0x77, 0x2c, 0x77, 0x2d, 0x77, 0x2e, 0x0f, 0x0d, 0x0a, 0x1b, 0x24, 0x29, 0x41, 0x0e, 
                0x77, 0x2f, 0x77, 0x30, 0x77, 0x31, 0x77, 0x32, 0x77, 0x33, 0x77, 0x34, 0x77, 0x35, 0x77, 0x36, 
                0x0f, 0x0d, 0x0a, 0x1b, 0x24, 0x29, 0x41, 0x0e, 0x77, 0x37, 0x77, 0x38, 0x77, 0x39, 0x77, 0x3a, 
                0x77, 0x3b, 0x77, 0x3c, 0x77, 0x3d, 0x77, 0x3e, 0x0f, 0x0d, 0x0a, 0x1b, 0x24, 0x29, 0x41, 0x0e, 
                0x77, 0x3f, 0x77, 0x40, 0x77, 0x41, 0x77, 0x42, 0x77, 0x43, 0x77, 0x44, 0x77, 0x45, 0x77, 0x46, 
                0x0f, 0x0d, 0x0a, 0x1b, 0x24, 0x29, 0x41, 0x0e, 0x77, 0x47, 0x77, 0x48, 0x77, 0x49, 0x77, 0x4a, 
                0x77, 0x4b, 0x77, 0x4c, 0x77, 0x4d, 0x77, 0x4e, 0x0f, 0x0d, 0x0a, 0x1b, 0x24, 0x29, 0x41, 0x0e, 
                0x77, 0x4f, 0x77, 0x50, 0x77, 0x51, 0x77, 0x52, 0x77, 0x53, 0x77, 0x54, 0x1b, 0x24, 0x2a, 0x48, 
                0x1b, 0x4f, 0x3b, 0x6b, 0x1b, 0x4f, 0x3b, 0x71, 0x0d, 0x0a, 0x1b, 0x24, 0x2a, 0x48, 0x1b, 0x4f, 
                0x3b, 0x63, 0x1b, 0x24, 0x29, 0x47, 0x1b, 0x4e, 0x63, 0x77, 0x1b, 0x4e, 0x5f, 0x34, 0x1b, 0x24, 
                0x2a, 0x48, 0x1b, 0x4f, 0x3b, 0x67, 0x0d, 0x0a, 0x1b, 0x24, 0x29, 0x47, 0x1b, 0x4e, 0x63, 0x79, 
                0x1b, 0x4e, 0x5f, 0x30, 0x0d, 0xa0, 0x1b, 0x24, 0x29, 0x47, 0x1b, 0x4e, 0x5f, 0x3a, 0x1b, 0x4e, 
                0x63, 0x7a, 0x1b, 0x4e, 0x24, 0x41, 0x1b, 0x4e, 0x24, 0x44, 0x1b, 0x4e, 0x24, 0x45, 0x1b, 0x4e, 
                0x24, 0x46, 0x1b, 0x4e, 0x24, 0x47, 0x1b, 0x4e, 0x24, 0x48, 0x0d, 0x0a, 0x1b, 0x24, 0x29, 0x47,
                0x1b, 0x4e, 0x26, 0x34, 0x1b, 0x24, 0x29, 0x41, 0x0e, 0x22, 0x4f, 0x22, 0x50, 0x22, 0x51, 0x22, 
                0x52, 0x22, 0x53, 0x22, 0x54, 0x22, 0x55, 0x22, 0x56, 0x22, 0x57, 0x22, 0x58, 0x22, 0x59, 0x22, 
                0x5a, 0x23, 0x40, 0x23, 0x41, 0x23, 0x42, 0x1b, 0x24, 0x29, 0x47, 0x1b, 0x4e, 0x24, 0x49, 0x1b, 
                0x4e, 0x24, 0x4a, 0x0d, 0x0a       
            };
         
        try{
            CharToByteConverter fromUConv =  CharToByteConverterICU.createConverter("iso-2022-cn");
            ByteToCharConverter toUConv = ByteToCharConverterICU.createConverter("iso-2022-cn");
            this.testToUnicode(toUConv, uSource,gbSource);
            this.testFromUnicode(fromUConv, uSource,gbSource);
            this.testSmBufFromUnicode(fromUConv, uSource,gbSource);
            this.testSmBufToUnicode(toUConv,uSource,gbSource);
        }
        catch(UnsupportedEncodingException ueEx){
            System.out.println(ueEx.toString());
        }
    } 
 
}