package in.cbslgroup.ezeepeafinal.customexceptions;

public class CantFindViewInViewHolderException extends Exception{

    public CantFindViewInViewHolderException(int pos) {

      super("There is no inflated view check as the view has been inflated in the recyclerview at postion "+pos);

    }
}
