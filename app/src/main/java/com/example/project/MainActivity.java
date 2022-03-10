package com.example.project;

import static android.content.ContentValues.TAG;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.project.model.AppDatabase;
import com.example.project.model.Course;
import com.example.project.model.User;
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.nearby.Nearby;
import com.google.android.gms.nearby.messages.Message;
import com.google.android.gms.nearby.messages.MessageListener;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Locale;

public class MainActivity extends AppCompatActivity implements GoogleApiClient.OnConnectionFailedListener, AdapterView.OnItemSelectedListener {

    SignInButton signInButton;
    private GoogleApiClient googleApiClient;
    //TextView textView;
    private static final int RC_SIGN_IN = 1;


    public int currentYear;
    public int currentQuarter;

    public RecyclerView usersRecyclerView;
    public RecyclerView.LayoutManager usersLayoutManager;
    public UsersViewAdapter userViewAdapter;

    protected List<Course> courses = new ArrayList<Course>();
    protected Course demo1 = new Course(2020, "spring", "CSE110", "tiny");
    protected Course demo2 = new Course(2021, "winter", "CSE101", "medium");
    protected Course demo3 = new Course(2021, "fall", "CSE2", "small");

    //protected User user1 = new User("Luffy","data:image/jpeg;base64,/9j/4AAQSkZJRgABAQAAAQABAAD/2wCEAAoHCBUVFRgWFRUYGBgZGBoaGhkcHBkaGhwaGhgZGRgaGhocIS4lHB4rHxgZJjgmKy8xNTU1GiQ7QDs0Py40NTEBDAwMEA8QHxISHzQsJSs0ND00NDQ0NDQ0NDY0NDQ0NDQ0NDQ0NDQ0NjQ0NDQ0NDQ0NDQ0NDQ0NDQ0NDQ0NDQ0NP/AABEIAQoAvgMBIgACEQEDEQH/xAAbAAAABwEAAAAAAAAAAAAAAAAAAQIDBAUGB//EAEUQAAIBAgMFBAcECQEIAwEAAAECEQADBCExBRJBUWEGInGBEzJSkaGxwUJy0fAHFGKCkrLS4fHCFSMzNFNzouJDY9MW/8QAGQEAAgMBAAAAAAAAAAAAAAAAAwQAAQIF/8QAKxEAAwACAQQBBAEDBQAAAAAAAAECAxEhBBIxQVETIjJhgRQjkXGhscHw/9oADAMBAAIRAxEAPwDjlHRUdQgBR0BRirKBFHFHQUVCBpTyCiCzSyhEdROtU6NKR2RpwpT2Ac1060wq08gMxz4Vh1o2p36GXtxRKgGtS8TZYANwyFIZCwyEZVaraLcaeiOWLEZAADIRkB159SaDMTlAHHIZnz5UGEUFk61rgxyFMCmiacZKQoq9mWgEDwpDLT26KOABJ8h9amytDIpLGltSDVkCNFRxRVRAUKFGasgmhQoVksFHQo6soVRqM6ICnYEcZkcojjPwyqEFFuHLlpTiim7YzqXYtTQreg0JsEcqdt2CYirHBbJd4IUxzrUYDYaKO+fz4Unl6iZ49j2Lparl8IyVjZzuYUMfLKpx7P3RoprdYe1bT1QSfCKeFwcF+NKV1eR/iuBpdPiXnb/2Oc3Oz9wmQhInP/HKhiNiOI7hjTSui+m5KBSDdzzUVa6vL8Iz/TYnvSf+Tlj7PcZMhioV3CkHLSuvultxDLHxFUmP7OI2aZeGnjRY67n7loFfQpr7X/k5ytuFO9rwqMa0W1NkshI161TXsOV18fKuhjyza2mIZsNQ9NEWiIpxlim6MhfQKKKOhFWQKhQK0IqEEUBQo6osApQFEKUDUKDQx9PGlohY/Mn5k0gZ1IsjOOE/kmss0ltkqxZ4Aa+8+PLwrVbL2PC775ACT9MuJqPsDZ6gekfJRp48IHE8qvrWNW5AAyBG6kiScwC3xOUxGvCuZ1GWqfbP8s6/TYZiVV+X4QtHc92ICiSAMwOCiNWPw4a1ITCsY3jrmwB0HBR9T09z2Ely24hcDLuiAWHrEucumvA61Y4TZb7vfcAnM7uZk695shyiDSynXkPWb0iMEpW7Vi2AtL65J+85+QIHwpIXDDRE/gn6VFoC8jZA3aG7VkLlj2VH7n9qNVsPluofFQPmKn8E72VDEaDMj4eJ4VFvYZ4J3iW1UaKCM4A8ok55nSp+1sRgcOJuOqHgiM28fBFP0rDvta9jbww+G31tudWjf3R6zMyjJY4eGedFjC65XC+WZfUJcPl/BocRbU77wCpAbLMTo8eUHxmqXHbBVi27qADHjoR0rSXcK9kBd3dQJuhtUlY3JI0mTrGlHauKWkjIopn9kk/Ig++g7qOZY0qjItUtnMsfs8oYNVzWjXQ+02z1I3lGfSsNdSDFdPps7yTs5fVYFjrjwyN6E5ZU4bGVP23jU05iWBzFHdvegKxz2t7K5kpo1Na3UVkiiy9gXOhkUqgBSgtWZEilUKcUVREBFq72Ls3faSYA1J086q7CS2Q41ttgbNYgGCcwBAmOZHAGJ7x8KV6jL2zw+RzpcSqtvwi2wWzS/cQSAIz9VAREnmxHDWOVPba7NEWA9pj6WyJUQgRgI3kCxnI5k55VoUdLKAQJ4KOf18eNYztJ20VCUtw7jke4h8R6x8PfXPwq6paHM+Ra54RX2P0jX0UK9q2xGUgshy4RmB5RSLv6R7zZeiQDkGYfSsZjcU1x2do3mMmAFE+AppFJrprp8flo5v1bXhmrxHbe+wIVEQn7XeYjqJMVnr+0brGWuOT1ZvlOVdA7N9mktWj6ZAz3Fhwc91D9gcjzPPwrDbf2UcPdZDmuqt7SnTzGh8KrE8fc5lG8s5FKpsjptK6vq3bg8HYfWlvti+wg37pHLff8agUKN2r4Adz+RZfjzrT9m+1CYMHcsbzN6zs0Ej2RCndWsrQFSoVLT8FqmntHVML+ku0cnw7r1Vlce4xU+zi8Fiv+XuqjlWX0bygYNEgKcgZGq9cq5rsLC4Z2/wB/eKclAgHxcyB7q6Ds/YGDSD6IOvMszeYMxXPzY8ceNr/gcwXkfPA/e3gvo3XvgCQdZiJHNTnBrBbasFXPwrrGNwSXLY3Mt0dxsyV6Z5kdK5z2kttnvCGBzHDxHSl+lrtyaXsdzayYXvyjLA50TmiY50GFdb2ch+BJuZU0WmjZaSVPKiIG9gWpKWyQTBIGZMaDrTC61YYa6EVgwBJGXNfPhPL/ABVU9eC5W/JAAzqSEmp3+zrfo2dbgnUIRDRxAzgxUBbTNO6CYHCqm5e/0XWOp1+/5L/s9hA7gDM8enjW+a8mHT1gsDMmAFHWuP2MU9syjshHFSR8qGLx9y4Zd2c/tEmk8vSPJfc3wN4+qmMfalyaPtF2qe7Nu2xVftOJluYHEL8T8KyjNSSactWyxAAliQAOpyA99NRExOkK3dZHtj1jCko9w+qsCebMYVfmT0HWtH2F2Rv3Decd22e7PF+H8Iz8SKX2mwYw+Hw+HHrFmdyOLhQD8WIHgK2WwsALFhEjMLLdXObfHLyoOXL9m17GMOH+5p+vJPNUPbDZXp7BZR37cuvMiO+vmM/EVf0KSi3NJo6FwqlyzhrCrfYOy/1j0qr66299PvKw7vmDHuodptnegxDoB3Sd9futmB5GR5Vbfo8/5h/+0f50rpVX2dyOTEf3FLMmyxSa1HbjZnor/pFHduy3QOPWHnkfM1lzWppVKaM3LmnLDBq32Jt27h27p3k+0hJ3T4eyeoqnoCrqVS0zM05e0dm2TthbqC5bMjRlOqnirfjUHtDYW4hYeXToa5/2e2s2HuBsyjZOvNeYHMV0V3WN6ZRgDIzyiQfdXI6jC8Vqp/g7PS5Zyy0/Ps5tibBVj0qM61s+0exN3vp3gQJgaH61lmw8U9hzK52I58DimvRFCcTRgeHnP0pZnSDSXU8AaYT2LNaG7Q9/50pz0bKAwyzgHqMzHOMvCRScPx8OQ48p08am+jLgQCSMtfkOHGqt6LidolYDCXGUQsTMORl1z+FarYWxVQbzaDUkanw+lI7MbPuSpYkomgnIE6wKn9tNrCygVIDtxHOM28h8SOVcvLkrJf05OriicMd9edf4MX2ut2FuRayeTvqPVB4D73MCs2a0OA7OX76lz3EgnfecxqSBqfGs+1dPFpT273o5WVuq7mtbCrWdg9nb943GHdtCRy32kL7gCfdWUFdP7IWltYZCfWuEvABLEHJchnG6B76znrtjj2b6ae61v0R+0Vn0mOwqHMZsfANvH+StXVDiVY4yy+4wi1cgHdkwV0z/AGuPOrcYpR64ZPvCB/EJHxpG+Zlfo6ULVN/sfFA0KFDCGR/SDgN62t4DNDut91vwaP4qqv0dr/v3P/1/61/CtxtPCi7Ze2ftIw84yPvisT2DZle7urLFVHQd4yWPADLqabi94XPwJ5I1nVfJpO2OB9JhnP2khx+76w81JrlZFdoODVh3++TIO9oJ13V0X51yM4I+l9EJn0m51nf3ZrfS39rXwD6uPuTXsVtPZxtLaY//ACWlfzJMj3bvvqvredvNnIlq0yiN1tyJMQVkQNBmnCsGaPjruWxbLHbWiy2I9v0gS8O4/dY6FCfVcHhB+BNb/Z2GeypsFt8J3rbe0k95DyYT8RXLxXSuzWKa/Z3gRvpAb76CFbwZIB8KB1U/bv0MdJSVa9lzhoe2U4ASk+wco8jl4EVi9tYIo8anPQfn8itm2DBbOd3ekKDGZg6jOJExzqB2qsOqggmOMZD4VzcVdmTj2dLJPfDXwc+uKZg/GkNaM6TUi5bJOhz0EZmp1j0dseqrtkCCTujwKkAnLWeOU611+7SOR2bZS2ASNMpjxP1NX2ykAIAzJ48AP2eZ6/5qhWAIAz4n6AcutW+xy28M+XWs5/wZvpvzSOqbHw4W2uUZSfnWau7OGIxT3LglEIVFOjNG8T1AJ8zWjQMUIB3QEyOpJjUjgo+PhrEYpbQsxCqozYngOZrj4W1yvLOllSbe/BVdrcd6LDOAe8/cX971iPBZ+Fcrar7tNtY4m4SoO4ghB0nNm5EmPhVAa7ODH2xz5OTnyd9ceByypYgDUkAeJyFdmwGDW0iovAAEnUwIz/DhXINmuFu2ydA6E+AYGu0mhdU3whjokuWRb9mXtuPs74Pgy/iq1Jis5tPtKoY2rKXLzzB3CQAeW8ASfIU1Y2rjUIL4S4U4wwZwOkAGeh+FL/Spoa+rKev+jSJZCnuyB7I9XxA4eUUnE3SnegFR63MD2hzA4ihhsQtxQ6HI88iDxBBzB6VB27iO56Jc7l3uKo13Tk7nkoBJmsJN1phHSS2WYqg7LYQI+KaMjfZR4LJ/1Gr8VGugWkcqNSzHlLElmPQa+UVc1w5+SqlNqn6Hbt9VIBOZ0AksfADOKyGy9m720bzxC22L5iO+6ggfFj5CnScVeJ3XNhCfYZrzcmYKO7I0EiBT9js9iLZLWsYd5iGYOphjEd6SeAA0mjSlCa3y0Atu2np6TJPbe3vYRz7LK3/kAfgTXLCK6lt68zYO6t1Qr7sboMgkEHeQ8V+XGuffqkYVrhHrX0QeCo7NHmR7qN0z1On8i/VrdJr4K0VouyG1RYuw5hHG6x4Aj1W+h8azta/snsxMRZuo4gq6lXGq7ywY5ju6UXK57X3eBfCn3LXk3yOCYGca8p5U7irO+kHPnMnTMZDU1m9lJesEWrql1kBLiyVifVcfZ8/jWtsHeBnofz7q4mee18HZxW2tnM9s4YqSRMnUnI9VEaCqRXbhJPQcK2/aXDgFi2moHE569F6+6ayDWp0ZUHNsgTyAznx+U10Olyd0civV41Nbn2ViIav9kqVKgiDyA72ZnPrnVbhk1gcPPr5aVoNj4YkqwjJgDOp/M0TqbSh7B9JjbtNG+wxPo8+Q+Yql2nsQYg9+6+4Dki7oX5Znqa0FlQEIPLOPlUVuNcrDTnlD+VKtp+DH9q8Daw+E3baBd90BOrNEtmTr6tYAiujdv1JsIeVwfFWrN3dlbuAF6O890MeiDeRR7zPnXXwX9ibflnKzxu2l6RV2sIWsm4uqMFfmA2aN4SGX3V1EqcRhV3WKG5bXvDhIEiuf9kMWqX9xwCl1TbcHTP1Z88v3q6NszB+hQWkl+83o1HrbupBJyhSdT051nqG9pe/QXpUu1v17BsvZlvDruIse032mPMn6U/cwqMZZQ3jmPIHIVYpsy6dSi+bN8gKcGyn4un8Df1UD6dt7GFmxpaRWogUQBAquu4Rv1pLoAKeja22eane3gc9QdMq0n+ym9tf4D/VRjZTe2P4P/aosdr0U82N+yuBzo4qx/wBkn/qf+H/tRjY443H8twf6Zqvo0T+pgpLi3Y7pSZGoYZTnxOcUV6y5Ug3AuWqrHxJNaAbJtcd8+Lv8gRT1vZ9pdLaTzIBPvIJrawv5MPqp9IxW2FT9XuhCGd0ZRnvMxOUTMnWs12qwfoMFh7XEOWb726S3xaut4/Dh7bIR9kkH2WAlSORBg1yX9IGI31w45qz+8L/eixLmkv8A3gHdq5de9aMTW5/R02V4fc/1VjcHaDOinRmQHwLAH510Ts9sz9XxF9NVKoyH9mWy8RpW+oa7GgHTy3aZo6lYVgoJOsZD6mop0ovShVYk9JrjZPB2InfBlO0uK3naSTHDrp5+NZRt05uQOA4/SrDbN+LjGZzy/E/h+TS4i8Sfpw8prqdNj7YQj1ebd8eiytYwsoRVCJlKrPePtOx7zGdATA4AVqdk3EQqHgZZDiKx+Gu7hBEyNIq42Ri2RxdFoOFbPeBZSWUxvDwk+VV1OPuk30uTsZ0ewQe6DkR7gRVfavHuhte8pP7aajzAJ8qY2Ljg44LnA0idSF6DLwp3HW4fiA5BB9lx6reBiD5DjXKx/bXax/JPPBG2zhBiLDoPWnuzwdGyHnEedMbJwaXcLasOIVhuPzXc3mfwbuGrA2i2ZEb2Tr1GjqeYgZ8o5U/aAUqX9qHYZZOpRm6QGnyp2b4S/YlWPl1+jOp2ew63rd/DhvRKwFy05JO4wgsCcxk29HSQa3ezMMEuXBruhFBOZiXMz1y91Z64hW46tvMqqbbZCYaCDC+ssbwkDLOrrZt0OULZ+kt7jHMf7yyxB6gkMW/do826pbBZJUS+32W1u5LOvFd33MMj7w3upSvJYciPkD9arb1h1vJuXDJR531DDdXcylSpObCCZOvPNYF0XiAUO8ktkwjdaFMTqZb+GmBMsqIUxFz2kH7rf1UW5d9q3/A3/wClQhJoCo4R+NweSAfMmi/V2Ot1/LcHySfjUISRUHDYpFTvuPXcCTmQHZVganIAU6MEnEu33ndvgTHwpGz8GtoMqqqjfYggCSGO9n4EkeAFQhCs40m1dubpCRcYE5ExIUBdRkucxmYisJtjY63sRuu5W1Yt20ePWZ2BcInWGX4ZVvtphVVU+yztcfpbU77+9oHmaytwtvekUT67uRGVxyO6rEEFo7uQyAAyoVXquBnFjVTz4KLtJ2YS2LV7CIwBt77W2JZgyMJImZ4SJ4ZVq8HiFuolxdGWR0nUe/5VMxYhkTOUt5yZgu29BPOFHvFVKNuMdwQGJYLoGPFz7CfM/FfJkdrT9B8eNQ9z4ZYFwNfz5VXYzGhULAiJMk68sjzo7lxmEr9ruoPH7Z+cch1qh7T3AiBATkB/n60qp77SHE1EOn8Gc2ne323oAngMgKrt5RqCfAx5zB91LuXBERUUmu1E6WjiZK29kwPGkZ/jwqwsbTcWxbGS7xJA9Zt4AEMfZyGXTjwVgrFthBJDchofM02+Fzj/ABQ6uW9NDawZJlVL8/BKwu1X3wZ0AUAaAD7IA4fPOt/gcWLqDnWU2Bs3DsQtwjeYwDOojTKtJiLItNbCtwkrwjQRH3TXL6py61K0zqdNFOdU+R9HYMysM8yvIj8Qcj5Ua4lSBOUkqZGjcVbkfnVi+HW6oZcjrPEEcagvhDPfWDoSIhhyIPyPkaHFr2CudjXpA2rEMg3Q8End4JdXUgcG5e8ydnXt5mQOhdiLlojQXEEMpzPrLw5B6QloLoOnEmOUnhRueZjjMxBBkEHgaZnJyAqONGhwtxbkPBDqGUrxUmN5T5qM+NPC0N8vOZUKR0UsR/Mazb4/dZWdxbuEhVuaI54LcGik+4nQqaucNtMTuXB6N9Mz3G+6x+Rg+OtORkTXIheJy+CwoUKKaICDopoGhUICm8ReVFLOYUan5COJJ0FR8XtFE7o77+wsEj7x0UePlNV+MupbT9YxbhETNU4A8Aq6sx9/gMqw7XheTahvl+CNtO6xSXQlr53dwfZtJ3iDwzO6CdO+elRLThWG8FZlgrbX1V9neMZDqRwyE1V4Lar4zEXXZWRERFS3JBCtLAtGrEZ8sxVi1kgQhCLGgA3iehOQ8YNLZHp6fkexac8EgTJLGWY7zHmT8uQHAAUTqDqMjr4VBMp1bWJIRebOxzY/mBrTmF3m7zMQgMyci58Psr040tda52MxGyctsKS7RkuXQak+Jy91YXtXdRzKr58+AznLwyNXW3Nvid1chEHmT05VkMTfkmRE5Efn31vpYpV3svMp7HLfLKq5bk5fP6a1GcQakXhw5U0TzzrsS+DhXOmXNi9kAFB56g1Pt2mKM4XIa6ccvrVR6bSNedXGAedDrqOdJ5p7Vs7PS07fbsk7CNtLiswYgNoCAd6IGvDOtdi7KX7ibsLcXI7xI3kE6QPWGvCZrF7TwDWgGnJs4EEDiDkcsqn7E2w5uqAquTkd47pECciTSeWHX9yXvgZjUNy+GvGvZtGxgsKAykHhpmOYqRY2klw7pEHyy86yvam7iCyOygIFMboZgsHPfbzXOq3Ze3mT1sweQy07uZpf6Na7lyEURc8+TWbRtODKSyxBXKRyKzofGq9SXyMzoTA05XLbfMfCqz/+gIbXLrw6Rr51ZYLbmGeN9jbY8WB3feJA+FEmbS8A8uNStb2O4zAq9p7R0ZSOcHgRPIx7qjdm7V9LKpdbfBRjuPnugFQFVtRk2hkdKvDhjAZCHU6MpkRQwVubqrzS4Pgv1ijTkeu0RqZ3sYtYw28g7oPZeCngCZAHQEeFWC7TuR9g/un+qoTVG/VgPUJT7sR/CZHwrauvk08MP0WbbRu+0g8EP1aot7EFsrl5vuzu+5UAZvDOowsE+s7HoIUf+Ofxp21bVfVUCdYET486juvkiwyvCJmFcLlaQLAJLuIAAEkrbB+ZBrC4jBYjG2kv3XZna4CAcgtkmDuqMhz5kVvQkYe8/H0bgfwH8ard0KAoyCgAdAMhUVuVteQbxqq0xi7hlmVUTkJMgQMhIHrROQPOmnxO4MjvHixyUfj4D3042PtTBuoD94fSmMdjLCgFXFxuAGar18epoFOvgbx40SrR3oZoCaneyLeR0E8TWe7QbagEIcuHL89KjYvaDtPeaB0/GsxjMUXJnmc63g6d1W6N58qxRx5fgF/GFmkkk0m7fGvw/H6D39YhpM8q6iifRxXmv2Ou0mabpJNFWkgVVskW3NPYe7DDOM6hq1OiQa1Upm8eRy00ad75ZVQtkYPuyoW7C74A7qnUzmD18/hVXh3yEmnWxQBrnvG19snfjJFPvr9HROzW0GCm25D5kgydDrMjoPfVD2gwKW3aGEkl93Pu7xn61T4HaO6e65B8xRbUxxaWZpZuNBnDfcMp453afGvH7K7EYjvGKZF6IgmeNMFvnQAp9QktHIvPVVsu9k9oLtgyjcc1+w33l08xBroHZbbxxdxmNsJ6O2Zht6S7L0EDuGuToK6h2HwvosG9wiGusY+6vcX47586DliUu7XJjudeSY7Z0VExogaUGRU0BSN6gDVllrijGDuEf9K4fgx+lcuxu1vSZu8/sjJR+7+M11bDrv2CntK6e+R9a4dcUjI6jI9CNaYwyqQurcU+N/6k448cqP05IlTVYTS7VyDR3jXoLHVPen4JbFmyz8Z+lQLqZ5CKs0uFR3eI+FRXjlWZ2mazY5c+SCw500RU50BqJeSKNL2c/Li7VsZJojQNFRBNi0NSUOdR1OUVJRMqpsZxTtjxuQIGtNTSacC1jwHdNgBo940VCoTbCUUukjWpmz8BcvOEtIXY8Bw6sdFHU1T+SIe2Ls5sRdS0urnM+yozZvIfGK6pjHRQtpBCIAoA6CPhVTsfZqYNCqkPecRccaKPYT6nj7gHi1J5b7nx4D449sUWoppM0DQwwotRTRE0W9ULLjY+IzKE65jx4j88q5v222UbGJcgdy4TcTzPfXxDT5EVsUuEEEHOpm0cKmOsm25CuM0bk0RI5g6EVvFXbX6F8sc7Rx4ikAZ1YbV2bcsOUuLusPcw9pTxFQBTqfAu/IsOYiabNKoqhbbYiaWYI+f9qIigoqyL4I109KbqRdSmCtbQnklpkm3bpxXjShcEZU2Ky3sb/HhDhM0M6AoVRABqOioVCbHEIBBImCDHOOFdE2TcAtD0DlEcTugLrxBJEzw1rnlq2zBiBkg3m6CQPmatuz+1vRNuue4xz/ZPteHP+1Cyy3PBvHUp8mylxxVvEFfiCflRm+w1Q+KkN8NfhRgzmNKMmk9jmgW7yt6pnnzHiNRS96mWtqSDGY46H3ilFqhaFM8ZnIUycSD6oZvAZe8wD5Up1BiQDGlHNTgnIjfc8FXxJJ9wj50pS/tgeCx8yaE0TOACSYAzJ6VZWiB2nxZNiLrb/BAVSd4zmDuyIHEVg6stt7S9M8j1FyUdOLHqfoKrKdxy1PIldS64BQmkk0JrejGwyaJGzmhFEahW/Y+CpHWo9y3QFS7QkVN6CJLJwxlhJJpBFBmoqhhvYamlUgUAasymLoUmlCqL2WNobuGc8XdU8kG8fjVcDVjjDFiyvPff3tAqvVZMASatsyvk0/ZjaZJ9ExkR3DyjVfCMx4VpZrEYXCG3FxjBQhoHQ5gnwrYW7oYBlMgiQRxFJZEm9yO4L2tP0Pk0zdPfQdSfcI/1UrepgmXHRD8SP6awkGZKJoTTc0U1WjQ4TWW7R7V3ibSHIeuRxPs+A49atts4w27TMPWPdXoTx8gDWFYk0xhjf3MVz5NfagyasNtmXDe2iMfEiD8qrgKm7ROVr/tJ8C1Mib8kGjoqKqNBzQY0KKrKbBUjDPE1HpQNUzcvT2gqFGW6URNWZCoCg1AVCvYdGGFFQTDM5IXMxMc+g61EuSrbS4LLahytLysp8ZqRsvDwN86nToP71S+kaQrk5d3PgAdPKTWktupA3SCOhoOdtLSMzW0Ix7wjdYHvIqTsO6yWwZlSxkHgJjeHLPWq7ajwoHNvkP7irXZ9uLaA+zJH3pJ+dYxz9nIxgW6ZczUaxPpbh6WwPIMfmaatX2QQQWHA8QOs6+OtN2cWu85AY5jIKeEjjkKx2NbG3S2tk+9dCKWYwBQe6FEkgDn+HOoLsWO82g9VeXU8z8qSqKNAMtOnhyq1Bfc/RXbau77IjSAxyHEDT3k/ARUcYC3yPvNK2yO+jfnJp+tPE1eRuUtCGT8nsjvs5DpI8DPzpvG4RmVN3PdQDlPeappaKrL21gI3RMIozyEiZ+daxO6T0BqlLRAdCpgiD1pFWmGu+mUh104jry61BxFgoYPkedFmue1+TU1tbGaHGhRCtlhzQoUBULBQo2oqhYIoA0KAqFb5DpSuQQQYI0I18ZpFGKhYeIuM7bzmTkNANPAUatGmRpNCo+TKlEm0z3HVTLZxGUxq3wFaS05JnmMhwVeE9fz403Zz/jD7rfSrnC/a++/8xob+A+FD5NRsKe8/3j/M1PmouH9Z/vH+Z6peGHf5L+SZvU27wROhynrypQpnFeo3gKpeTT8FZtq4GCRqCQRy8fdUgNMHmJpG1P8Ahn/uj+U0nD+ov3R8qxl/FCOT8hraQYoQsnMTHKo+zsIkb7hiwOSEQvQk8R0qxoVUZHM6QGltiLSBRA8+pOppOJs76kcdR407QFC7nvZozxqbh9nNvAOCFPsjeJziBGQz4nSo2M9d/E1c22JLSZ7/APRT68E2QcThFIJtpdBBGTLqCSO7Gf8Ag0WF2dcaYRgOZBUeEnU1a2z6n3v9D1d2f+Avj/XVU9BMS7nyf//Z",courses, 17);
    //protected User user2 = new User("Zoro","data:image/jpeg;base64,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",new ArrayList<Course>(), 20);
    //protected User user3 = new User("Nami","https://www.google.com/imgres?imgurl=https%3A%2F%2Fi.insider.com%2F6224ebce9908630019989e45%3Fwidth%3D700&imgrefurl=https%3A%2F%2Fwww.businessinsider.com%2Fukraine-plans-for-continuity-of-government-if-zelensky-dies-blinken-2022-3&tbnid=fHoGDb-jQQGF1M&vet=12ahUKEwiOurbq4bf2AhXoADQIHbJDBawQMygAegUIARCOAQ..i&docid=NvAs0rs0p0sHXM&w=700&h=525&q=volodymyr%20zelensky&ved=2ahUKEwiOurbq4bf2AhXoADQIHbJDBawQMygAegUIARCOAQ", new ArrayList<Course>(), 22);

    public List<User> fellowUsers = new ArrayList<User>();
    private TextView txtHelloWorld;
    private Spinner spinnerTextSize;

    public List<Course> myCourses;
    public List<Course> demoCourse;

    public String myId;
    public String fakedMessageString;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    /*
        this.user1.getCourses().add(demo1);
        this.user2.getCourses().add(demo2);
        this.user3.getCourses().add(demo3);

     */

        /*
        fellowUsers.add(user1);
        fellowUsers.add(user2);
        fellowUsers.add(user3);
         */

        SharedPreferences preferences = getSharedPreferences("USERINFO", Context.MODE_PRIVATE);
        String username = preferences.getString("NAME", null);
        String photourl = preferences.getString("URL", null);

        if(username == null && photourl == null){
            Intent intent = new Intent(this, SignIn.class);
            startActivity(intent);
        }

        GoogleSignInOptions gso =  new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .build();
        googleApiClient=new GoogleApiClient.Builder(this)
                .enableAutoManage(this,this)
                .addApi(Auth.GOOGLE_SIGN_IN_API,gso)
                .build();

        signInButton=(SignInButton)findViewById(R.id.sign_in_button);
        signInButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = Auth.GoogleSignInApi.getSignInIntent(googleApiClient);
                startActivityForResult(intent,RC_SIGN_IN);
            }
        });


        AppDatabase db = AppDatabase.singleton(getApplicationContext());
        myCourses = db.coursesDao().getAll();

        String usernameFinal = preferences.getString("NAME", null);
        String photourlFinal = preferences.getString("URL", null);
        myId = preferences.getString("ID", null);
        myId = "848985";
        Log.e("My ID", " "+ myId);

        currentYear = db.coursesDao().getMaxYear();

        List<Course> currentYearCourses = db.coursesDao().getCoursesFromMaxYear();
        currentQuarter = 0;

        for(Course c1 : currentYearCourses) {
            int quarterToInt = c1.quarterToNum();
            if (quarterToInt > currentQuarter){ currentQuarter = quarterToInt;}
        }
        Log.d("Max year:", String.valueOf(currentYear) + " " + String.valueOf((currentQuarter)));


        fakedMessageString = "B3%&J" + "Jon," + "https://photos.app.goo.gl/PizS3MAD4QCqGRNs5," + "825103,";

        List<Course> demoCourse = new ArrayList<Course>();

        demoCourse.add(demo1);
        demoCourse.add(demo2);
        demoCourse.add(demo3);

        for (Course c2 : demoCourse) {
            fakedMessageString += c2.courseToString();
        }
        fakedMessageString += ":908654,848985,";



        //Nearby.getMessagesClient(this).subscribe(messageListener);
        //Nearby.getMessagesClient(this).unsubscribe(messageListener);



        updateUserView();

        usersRecyclerView.setVisibility(View.INVISIBLE);

        // Dropdown menu using spinner object.
        Spinner spinner = findViewById(R.id.spinner);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.font_sizes, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);
        spinner.setOnItemSelectedListener((AdapterView.OnItemSelectedListener) this);
    }

    public void onButtonOpenHistoryClicked(View view){
        Intent intent = new Intent(this, ClassHistory.class);
        startActivity(intent);
    }

    private boolean isUserSignedIn;

    public void onSearchForClassmatesClicked(View view){

        TextView textView = findViewById(R.id.Search_for_classmates);
        String text = textView.getText().toString();

        if(text.equals("Start")){
            usersRecyclerView.setVisibility(View.VISIBLE);
            Nearby.getMessagesClient(this).subscribe(messageListener);
            Intent intent = new Intent(this, PublishMessageService.class);
            startService(intent);
            //Update userViewAdapter
            this.messageListener = new FakedMessageListener(messageListener, 15, fakedMessageString);

            updateUserView();

            textView.setText("Stop");
        }
        else if (text.equals("Stop")){
            unsubscribe();

            textView.setText("Start");
        }
    }

    private void updateUserView(){
        usersRecyclerView = findViewById(R.id.users_view);

        usersLayoutManager = new LinearLayoutManager(this);
        usersRecyclerView.setLayoutManager(usersLayoutManager);

        userViewAdapter = new UsersViewAdapter(fellowUsers);

        usersRecyclerView.setAdapter(userViewAdapter);
    }


    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode==RC_SIGN_IN){
            GoogleSignInResult result = Auth.GoogleSignInApi.getSignInResultFromIntent(data);
            handleSignInResult(result);
        }
    }

    private void handleSignInResult(GoogleSignInResult result){
        if(result.isSuccess()){
            gotoProfile();
        }else{
            Toast.makeText(getApplicationContext(),"Sign in cancel",Toast.LENGTH_LONG).show();
        }
    }

    private void gotoProfile(){
        Intent intent=new Intent(MainActivity.this,ProfileActivity.class);
        startActivity(intent);
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }
    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        String text = parent.getItemAtPosition(position).toString();
       // Toast.makeText(parent.getContext(), text, Toast.LENGTH_SHORT).show();
        switch (text)
        {
            case "recent":
                Collections.sort(fellowUsers, new SortByRecencyComparator());
                updateUserView();
                break;
            case "small classes":
                Collections.sort(fellowUsers, new SortBySizeComparator());
                updateUserView();
                break;
            default:
                break;
        }
    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {
    }
    //Set method for BDD scenario test
    public void setFellowUsers(List<User> fellowUsers){
        this.fellowUsers = fellowUsers;
    }

    //Get method for BDD scenario test
    public List<User> getFellowUsers(){
        return this.fellowUsers;
    }

    private void unsubscribe() {
        Log.i(TAG, "Unsubscribing.");
        Nearby.getMessagesClient(this).unsubscribe(messageListener);
    }

    public MessageListener messageListener = new MessageListener() {
        @Override
        public void onFound(@NonNull Message message) {
            boolean waved = false;
            String sMessage = new String(message.getContent());
            Log.d("Message found", sMessage.substring(0,5));
            if (sMessage.substring(0, 5).equals("B3%&J")) { //authentication key of message

                int k1 = 5;
                while (true) {
                    k1++;
                    if (sMessage.charAt(k1) == ',') break;

                }
                String fellowStudentName = sMessage.substring(5, k1);

                int k2 = k1;
                while (true) {
                    k2++;
                    if (sMessage.charAt(k2) == ',') break;
                }
                String fellowStudentPhotoURL = sMessage.substring(k1+1, k2);
                //Log.d("fellowstudenturl", fellowStudentPhotoURL);

                int k3 = k2 + 1;
                int k4 = k3 + 6;

                String fellowStudentID = sMessage.substring(k3, k4);
                List<Course> fellowStudentMutualCourse = new ArrayList<Course>();
                Log.d("FellowStudentID", fellowStudentID);
                for (int i = k4 + 1; i < sMessage.length(); i++) {
                    int oldI = i;
                    String fellowStudentSubjectAndNumber = "";
                    String fellowStudentQuarter = "";
                    String fellowStudentYear = "";
                    String fellowStudentSize = "";
                    Course fellowStudentThisCourse;


                    while (true) {
                        if (sMessage.charAt(i) == '%') {   //subject and course divider
                            fellowStudentSubjectAndNumber = sMessage.substring(oldI, i);
                            oldI = i + 1;
                            i++;
                            continue;
                        } else if (sMessage.charAt(i) == '^') {   //Quarter divider
                            fellowStudentQuarter = sMessage.substring(oldI, i);
                            oldI = i + 1;
                            i++;
                            continue;
                        } else if (sMessage.charAt(i) == '~') {   //year and course divider
                            fellowStudentYear = sMessage.substring(oldI, i);
                            oldI = i + 1;
                            i++;
                            continue;
                        } else if (sMessage.charAt(i) == '$') {   // size divider
                            fellowStudentSize = sMessage.substring(oldI, i);
                            fellowStudentThisCourse = new Course(Integer.parseInt(fellowStudentYear),
                                    fellowStudentQuarter.toLowerCase(Locale.ROOT), fellowStudentSubjectAndNumber.toLowerCase(Locale.ROOT), fellowStudentSize);

                            for (Course myCourse : myCourses) {
                                if (myCourse.equals(fellowStudentThisCourse)) {
                                    fellowStudentMutualCourse.add(fellowStudentThisCourse);
                                }
                            }
                            break;
                        }
                        if(sMessage.charAt(i) == ':') break;
                        else i++;
                    }
                    if (sMessage.charAt(i) == ':'){
                        Log.d("Checking ids", "jibbi");
                        String fellowUsersWavedHands = sMessage.substring(i+1, sMessage.length());
                        int j = i + 1;
                        while(j < sMessage.length()){
                            Log.d("ID", sMessage.substring(j, j+6));
                            if(sMessage.substring(j, j+6).equals(myId)){
                                waved = true;
                                break;
                            }
                            j += 7;
                        }
                        break;
                    }
                }

                if (fellowStudentMutualCourse.isEmpty()==false) {
                    Log.d("in first if", "v");
                    if(waved) fellowStudentName += new String(Character.toChars(0x1F44B));
                    User user = new User(fellowStudentName, fellowStudentPhotoURL, fellowStudentMutualCourse, Integer.parseInt(fellowStudentID), waved);
                    boolean alreadyAdded = false;
                    Log.d("size of fellowusers", String.valueOf(fellowUsers.size()));
                    for (User u : fellowUsers){
                        if(u.equals(user)) {
                            alreadyAdded = true;
                            boolean oldWaveStatues = u.isWaved();
                            if(oldWaveStatues == false & waved == true){
                                user.setWaved(true);
                                user.setName(fellowStudentName);
                                updateUserView();
                            }
                        }
                    }
                    if(!alreadyAdded) {
                        fellowUsers.add(user);
                        Log.e("Waved", String.valueOf(user.isWaved()));
                        updateUserView();
                        Log.e("add user", "s");
                        updateUserView();
                    }

                }
            }

        }

    };
}