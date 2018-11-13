package mx.nextia.segmentedstories;

import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import java.util.ArrayList;

import mx.nextia.segmentedstories.modelos.StoriesUser;
import mx.nextia.segmentedstories.modelos.Story;
import mx.nextia.segmentedstories.modelos.VIewPagerAdapter;

public class StoriesActivity extends AppCompatActivity {

    ViewPager viewPager;
    ArrayList<Story> al_stories;
    ArrayList<StoriesUser> al_stories_user;
    VIewPagerAdapter adapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_stories);

        setResult(RESULT_OK);

        viewPager = findViewById(R.id.viewpager);
        al_stories = new ArrayList<>();
        al_stories_user = new ArrayList<>();
        al_stories.add(new Story("https://fsmedia.imgix.net/2b/0f/03/43/0925/4b91/9c48/a14bdea9f716/super-smash-bros-ultimate-characters.png?rect=0%2C43%2C1395%2C697&dpr=2&auto=format%2Ccompress&w=650.jpg", 5000, 0));
        al_stories.add(new Story("https://scontent.fmex7-1.fna.fbcdn.net/v/t1.0-9/43135843_1947936055242481_8233990324100268032_n.jpg?_nc_cat=105&oh=0ef3941eb53cdce62a3d1cd68c057deb&oe=5C5E4947", 2000, 0));
        al_stories.add(new Story("https://fsmedia.imgix.net/2b/0f/03/43/0925/4b91/9c48/a14bdea9f716/super-smash-bros-ultimate-characters.png?rect=0%2C43%2C1395%2C697&dpr=2&auto=format%2Ccompress&w=650.jpg", 7000, 0));

        al_stories_user.add(new StoriesUser(al_stories, 0));

        adapter = new VIewPagerAdapter(al_stories_user, StoriesActivity.this);
        viewPager.setAdapter(adapter);

    }
}
