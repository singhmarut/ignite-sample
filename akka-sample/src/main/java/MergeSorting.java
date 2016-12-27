import java.util.ArrayList;
import java.util.List;

/**
 * Created by marutsingh on 12/26/16.
 */
public class MergeSorting {

    void mergeSorting(int a[] , int b[]){

        int length = Math.max(a.length, b.length);
        List<Integer> integerList = new ArrayList<>(a.length + b.length);

        for (int i = 0; i < length; i++){
            if (i < a.length && i < b.length){
                if (a[i] < b[i]){
                    integerList.add(a[i]);
                }else{
                    integerList.add(b[i]);
                }
            }
        }
    }
}
