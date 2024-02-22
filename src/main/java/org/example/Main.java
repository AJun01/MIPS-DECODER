package org.example;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

/*
 *  This project serves as a practical tool for understanding
 * and translating MIPS binary code into a more human-readable
 * form, aiding in the analysis and debugging of MIPS assembly
 * language programs.
 */
public class Main {


        //1. set beginning address starts at 9A040

        public static int addRess = 0x9A040;


        //2. hashmap to store opCodes and functCodes

        //first one is the opCodes map

        public static Map<Integer, String> opCodes;

        static {

            Map<Integer, String> tempMap = new HashMap<Integer, String>();

            tempMap.put(0x23, "lw");

            tempMap.put(0x2B, "sw");

            tempMap.put(0x04, "beq");

            tempMap.put(0x05, "bne");

            opCodes = Collections.unmodifiableMap(tempMap);

        }

        //second one is the funcCodes

        public static Map<Integer, String> funcCodes;

        static {

            Map<Integer, String> tempMap = new HashMap<Integer, String>();

            tempMap.put(0x20, "add");

            tempMap.put(0x22, "sub");

            tempMap.put(0x24, "and");

            tempMap.put(0x25, "or");

            tempMap.put(0x2A, "slt");

            funcCodes = Collections.unmodifiableMap(tempMap);

        }

        //3. data masking methods for registers and destination part of the whole hex address

        //getRegNum takes the binary integer from masking methods, then returns the register number as string


        public static String getRegNum(int reg) {

            return '$' + Integer.toString(reg);

        }

        //masking methods for register and destination : Reg1, Reg2 and Des

        public static String getReg1(int address) {

            int reg1 = (address & 0x3E00000) >>> 21;

            return Integer.toHexString(reg1);
        }

        public static String getReg2(int address) {

            int reg2 = (address & 0x1F0000) >>> 16;

            return Integer.toHexString(reg2 );
        }

        public static String RgetDes(int address) {

            int des = (address & 0xF800) >>> 11;

            return getRegNum(des);
        }

        //masking method for function codes

        public static String RgetFunc(int address) {

            int func = (address & 0x3F);

            return funcCodes.get(func);
        }

        //masking method for operation codes

        public static String opcode;

        public static String IgetOpc(int address) {

            int opc = (address & 0xFC000000) >>> 26;

            opcode = String.valueOf(opc);

            return opCodes.get(opc);
        }

        //masking method for offsets

        public static String IgetOffset(int address) {

            short offset = (short)(address & 0xFFFF);

            return String.valueOf(offset);
        }

        //getBranch from address, used when beq or bne
        public static String getBranch(int address) {

            short offset = (short)(address & 0xFFFF);

            int branch = (addRess + 4) + (offset << 2);

            return Integer.toHexString(branch);

        }


        //after CheckFormat tells if the address format is R or I,
        //the address as binary integers, will goes to those masking and mapping methods, then returns as one string,
        //so it is actually the final disassemble method that takes the hex address and outputs instruction as string

        public static String CheckFormat(int address) {

            addRess += 4;

            if ((address & 0xFC000000) == 0) {

                return(Integer.toHexString(addRess) + " " + RgetFunc(address) + " " + RgetDes(address) + ", " +  getReg1(address) + ", " + getReg2(address) );

            }
            else{

                IgetOpc(address);

                if (IgetOpc(address).equals("beq") || IgetOpc(address).equals("bne")) {

                    return(Integer.toHexString(addRess) + " " + IgetOpc(address) + " " + getReg1(address) + ", " + getReg2(address) + " address " + getBranch(address));
                }
                else {

                    return(Integer.toHexString(addRess) + " " + IgetOpc(address) + " " + getReg2(address) + ", " + IgetOffset(address) + "(" + getReg1(address) + ") ");
                }
            }
        }

        //running checkFormat under main()

        public static void main(String[] args) {

            //0x032BA020, 0x8CE90014, 0x12A90003, 0x022DA822, 0xADB30020, 0x02697824, 0xAE8FFFF4,
            //0x018C6020, 0x02A4A825, 0x158FFFF7, 0x8ECDFFF0
            System.out.print(CheckFormat(0x00A63820) + "\n");
            System.out.print(CheckFormat(0x8CE90014) + "\n");
            System.out.print(CheckFormat(0x12A90003) + "\n");
            System.out.print(CheckFormat(0x022DA822) + "\n");
            System.out.print(CheckFormat(0xADB30020) + "\n");
            System.out.print(CheckFormat(0x02697824) + "\n");
            System.out.print(CheckFormat(0xAE8FFFF4) + "\n");
            System.out.print(CheckFormat(0x018C6020) + "\n");
            System.out.print(CheckFormat(0x02A4A825) + "\n");
            System.out.print(CheckFormat(0x158FFFF7) + "\n");
            System.out.print(CheckFormat(0x8ECDFFF0) + "\n");
        }
    }
