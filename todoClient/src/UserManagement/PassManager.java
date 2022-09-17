package UserManagement;

public class PassManager {
    private boolean hasSpecialChar;
    private boolean hasCapitalLetter;
    private boolean hasSmallLetter;
    private boolean hasThreeNumbers;
    private boolean hasThreeLetters;
    private void setPassPropertiesToFalse(){
        hasSpecialChar=false;
        hasCapitalLetter=false;
        hasSmallLetter=false;
        hasThreeNumbers=false;
        hasThreeLetters=false;
    }
    private void setPassProperties(String pass){
        int numbersCnt=0,lettersCnt=0;
        setPassPropertiesToFalse();

        for(int i=0 ; i < pass.length() ; i++){
            if(pass.charAt(i)-'0' >= 0 && pass.charAt(i)-'0' <= 9){
                numbersCnt++;
            }else if(pass.charAt(i)-'a' >= 0 && pass.charAt(i)-'a' <= 26){
                hasSmallLetter=true;
                lettersCnt++;
            }else if(pass.charAt(i)-'A' >= 0 && pass.charAt(i)-'A' <= 26){
                hasCapitalLetter=true;
                lettersCnt++;
            }else {
                hasSpecialChar=true;
            }
        }
        if(numbersCnt >= 3) hasThreeNumbers=true;
        if(lettersCnt >= 3) hasThreeLetters=true;
    }
    private boolean isPassLengthOk(String pass){
        return pass.length() >= 8 && pass.length() <= 30;
    }
    private boolean doPropertiesMeetStandard(){
        if(!hasSpecialChar)
            return false;
        else if(!hasCapitalLetter)
            return false;
        else if(!hasSmallLetter)
            return false;
        else if(!hasThreeNumbers)
            return false;
        else return hasThreeLetters;
    }
    public boolean isPassStrong(String pass){
        if(!isPassLengthOk(pass)){
            return false;
        }
        setPassProperties(pass);
        return doPropertiesMeetStandard();
    }

    public String makePassHash(String pass){
        return pass;
    }
}
