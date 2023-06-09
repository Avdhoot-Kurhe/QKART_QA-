package QKART_TESTNG;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import com.google.common.collect.Table.Cell;
// import org.apache.logging.log4j.core.util.Assert;
import org.testng.Assert;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;


public class DP {
    @DataProvider (name = "data-provider")
    public Object[][] dpMethod (Method m) throws IOException{
        switch (m.getName()) {
            case "Sum": 
                    return new Object[][] {{2, 3 , 5}, {5, 7, 9}};
            case "Diff": 
                    return new Object[][] {{2, 3, -1}, {5, 7, -2}};
            }
            return null;
            
    }
    
    @Test (dataProvider = "data-provider")
     public void Sum (int a, int b, int result) {
          int sum = a + b;
          Assert.assertEquals(result, sum);
     }
      
    @Test (dataProvider = "data-provider")
    public void Diff (int a, int b, int result) {
          int diff = a - b;
          Assert.assertEquals(result, diff);
     }
}