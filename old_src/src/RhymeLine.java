public class RhymeLine {
    String text;
    String lastWord;

    public RhymeLine(OldMeterIndex ind, String meter) {
        text = makeLine(ind, meter);
        int i = text.length() - 2;
        boolean spaceFound = false;
        while (!spaceFound) {
            if (text.charAt(i) == ' ') spaceFound = true;
            else i--;
        }
        lastWord = text.substring(i);
        lastWord = lastWord.replaceAll("\\s+", "");
    }

    public RhymeLine(OldMeterIndex ind, String meter, RhymeIndex rind, String rhymeMe) {
        // this function currently tells the system that rhymeMe does not have a rhyme if it encounters ANY type of error.
        // This is for simplicity of creating a randomly generated poem.
        // Hope that's ok with you. Feel free to change if it isn't.
        String newLast = rind.getRhymingWord(rhymeMe);
        if (newLast.equals("WNFerror")) {
            text = "error: word not found";
            lastWord = "NRWerror";
        } else if (newLast.equals("NRWerror")) {
            text = "error: no rhyming word for " + rhymeMe;
            lastWord = "NRWerror";
        } else {
            try{
            while (ind.getTarget(newLast)[1] == null) newLast = rind.getRhymingWord(rhymeMe);
            String newMeter = ind.getTarget(newLast)[1];
            int k = 0;
            while (!meter.substring(meter.length() - newMeter.length()).equals(newMeter) && k < 3) {
                newLast = rind.getRhymingWord(rhymeMe);
                k++;
            }
            if (k >= 10) {
                //System.out.println("WARNING: Despite our best efforts could not find a metrical rhyme for " + rhymeMe);
            }
            text = makeLine(ind, meter, newLast);
            int i = text.length() - 2;
            boolean spaceFound = false;
            while (!spaceFound) {
                if (text.charAt(i) == ' ') spaceFound = true;
                else i--;
            }
            lastWord = text.substring(i);
            lastWord = lastWord.replaceAll("\\s+", "");
            if (k >= 3) {
                text = "error : no rhyme that fits the meter for " + rhymeMe;
                lastWord = "NRWerror";
            }
        } catch (Exception e) {
                text = "error : exception";
                lastWord = "NRWerror";
            }
        }

    }

    public String makeLine(OldMeterIndex ind, String str) {
        String iambx5 = str;
        String line = "";
        // ArrayList<String> pronunciations = new ArrayList<String>();
        while (!iambx5.equals("")) {
            String[] test = ind.getRandom();
            if (test[1].length() > 0 && test[1].length() <= iambx5.length() && test[1].equals(iambx5.substring(0, test[1].length()))) {
                if(test[0].contains("(")) {
                    test[0] = test[0].substring(0,test[0].indexOf("("));
                }
                line += test[0] + " ";
                iambx5 = iambx5.substring(test[1].length());
            }
        }
        return line;
    }

    public String makeLine(OldMeterIndex ind, String str, String lastWord) {     // lastWord guaranteed to fit the meter
        String[] last = ind.getTarget(lastWord);
        if(last[0].contains("(")) {
            last[0] = last[0].substring(0,last[0].indexOf("("));
        }
        String iambx5 = str.substring(0, str.length() - last[1].length());
        String line = last[0];
        while (!iambx5.equals("")) {
            String[] test = ind.getRandom();
            if (test[1].length() > 0 && test[1].length() <= iambx5.length() && test[1].equals(iambx5.substring(iambx5.length() - test[1].length()))) {
                if(test[0].contains("(")) {
                    test[0] = test[0].substring(0,test[0].indexOf("("));
                }
                line = test[0] + " " + line;
                iambx5 = iambx5.substring(0, iambx5.length() - test[1].length());
            }
        }
        return line;

    }


}
