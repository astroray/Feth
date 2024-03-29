package com.kyigames.feth;

import android.content.Context;
import android.content.res.Resources;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.PagerAdapter;
import androidx.viewpager.widget.ViewPager;

import com.kyigames.feth.model.Character;
import com.kyigames.feth.model.ClassCategory;
import com.kyigames.feth.model.Database;
import com.kyigames.feth.model.Loss;
import com.kyigames.feth.model.SideStory;
import com.kyigames.feth.model.UnitClass;
import com.kyigames.feth.view.CharacterContent;
import com.kyigames.feth.view.CharacterHeader;
import com.kyigames.feth.view.ClassCategoryHeader;
import com.kyigames.feth.view.ClassContent;
import com.kyigames.feth.view.ClassHeader;
import com.kyigames.feth.view.FactionHeader;
import com.kyigames.feth.view.LicenseItem;
import com.kyigames.feth.view.LossContent;
import com.kyigames.feth.view.SideStoryContent;
import com.kyigames.feth.view.SideStoryHeader;
import com.kyigames.feth.view.SideStorySectionHeader;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import devlight.io.library.ntb.NavigationTabBar;
import eu.davidea.fastscroller.FastScroller;
import eu.davidea.flexibleadapter.FlexibleAdapter;
import eu.davidea.flexibleadapter.items.IFlexible;

class PageManager extends PagerAdapter
{
    interface IPageInitializer
    {
        View createPage();
    }

    private static String TAG = PageManager.class.getSimpleName();
    private final IPageInitializer[] PAGE_CREATORS =
            {
                    this::createCharacterPage,
                    this::createClassPage,
                    this::createSideStoryPage,
                    this::createLossPage,
                    this::createLicensePage
            };


    private Context m_context;
    private LayoutInflater m_inflater;

    // Extra
    private FastScroller m_fastScroller;

    PageManager(Context context)
    {
        super();
        m_context = context;
        m_inflater = LayoutInflater.from(context);
    }

    private Resources getResources()
    {
        return m_context.getResources();
    }

    List<NavigationTabBar.Model> buildTabMenus()
    {
        final String[] titles = getResources().getStringArray(R.array.menu_title);
        final String[] colors = getResources().getStringArray(R.array.menu_color);
        final TypedArray icons = getResources().obtainTypedArray(R.array.menu_icon);

        ArrayList<NavigationTabBar.Model> models = new ArrayList<>();

        for (int i = 0; i < titles.length; ++i)
        {
            Drawable iconRes = m_context.getDrawable(icons.getResourceId(i, 0));
            int colorRes = Color.parseColor(colors[i]);
            String title = titles[i];

            models.add(
                    new NavigationTabBar.Model.Builder(
                            iconRes,
                            colorRes)
                            .selectedIcon(iconRes)
                            .title(title)
                            .build()
            );
        }

        icons.recycle();
        return models;
    }

    @Override
    public int getCount()
    {
        return PAGE_CREATORS.length;
    }

    @Override
    public boolean isViewFromObject(final View view, final Object object)
    {
        return view.equals(object);
    }

    @Override
    public void destroyItem(final View container, final int position, final Object object)
    {
        ((ViewPager) container).removeView((View) object);
    }

    @Override
    public Object instantiateItem(final ViewGroup container, final int position)
    {
        View page = createPage(position);
        container.addView(page);
        return page;
    }

    private View createPage(int position)
    {
        Log.d(TAG, "Create page view " + position);
        return PAGE_CREATORS[position].createPage();
    }

    private View createCharacterPage()
    {
        View page = m_inflater.inflate(R.layout.page_character, null, false);
        initializeCharacterPage(page);
        return page;
    }

    private View createClassPage()
    {
        View page = m_inflater.inflate(R.layout.page_class, null, false);
        initializeClassPage(page);
        return page;
    }

    private View createSideStoryPage()
    {
        View page = m_inflater.inflate(R.layout.page_side_story, null, false);
        initializeSideStoryPage(page);
        return page;
    }

    private View createLossPage()
    {
        View page = m_inflater.inflate(R.layout.page_loss, null, false);
        initializeLossPage(page);
        return page;
    }

    private View createLicensePage()
    {
        View page = m_inflater.inflate(R.layout.page_license, null, false);
        initializeLicensePage(page);
        return page;
    }

    private void initializeCharacterPage(View view)
    {
        FactionHeader none = new FactionHeader(m_context.getString(R.string.faction_none), R.color.colorFactionNone, R.drawable.ic_character);
        FactionHeader eagle = new FactionHeader(m_context.getString(R.string.faction_eagle), R.color.colorFactionEagle, R.drawable.ic_flag_eagle);
        FactionHeader lion = new FactionHeader(m_context.getString(R.string.faction_lion), R.color.colorFactionLion, R.drawable.ic_flag_lion);
        FactionHeader deer = new FactionHeader(m_context.getString(R.string.faction_deer), R.color.colorFactionDeer, R.drawable.ic_flag_deer);
        FactionHeader church = new FactionHeader(m_context.getString(R.string.faction_church), R.color.colorFactionChurch, R.drawable.ic_flag_church);

        addFactionMembers(none, new String[]{getResources().getString(R.string.hero_name)});
        addFactionMembers(eagle, getResources().getStringArray(R.array.eagle_members));
        addFactionMembers(lion, getResources().getStringArray(R.array.lion_members));
        addFactionMembers(deer, getResources().getStringArray(R.array.deer_members));
        addFactionMembers(church, getResources().getStringArray(R.array.church_members));

        List<IFlexible> factionHeaders = new ArrayList<>();
        factionHeaders.add(none);
        factionHeaders.add(eagle);
        factionHeaders.add(lion);
        factionHeaders.add(deer);
        factionHeaders.add(church);

        FlexibleAdapter<IFlexible> adapter = new FlexibleAdapter<>(factionHeaders);
        adapter.setDisplayHeadersAtStartUp(true);
        adapter.setStickyHeaders(true);
        adapter.expandItemsAtStartUp();

        RecyclerView pg_character = view.findViewById(R.id.character_list_view);
        pg_character.setAdapter(adapter);
    }

    private void addFactionMembers(FactionHeader factionHeader, String[] memberNames)
    {
        for (String memberName : memberNames)
        {
            Character character = Database.findEntityByKey(Character.class, memberName);
            CharacterContent characterContent = new CharacterContent(character);
            CharacterHeader characterHeader = new CharacterHeader(factionHeader, character);

            characterHeader.addSubItem(characterContent);
            factionHeader.addSubItem(characterHeader);
        }
    }

    private void initializeSideStoryPage(View page)
    {
        final String[] sideStorySections = m_context.getResources().getStringArray(R.array.side_story_sections);
        final String[] sideStorySectionColors = m_context.getResources().getStringArray(R.array.side_story_section_colors);

        List<IFlexible> sideStorySectionHeaders = new ArrayList<>();

        for (int i = 0; i < sideStorySections.length; ++i)
        {
            String sectionName = sideStorySections[i];
            String sectionColor = sideStorySectionColors[i];
            SideStorySectionHeader sectionHeader = new SideStorySectionHeader(sectionName, sectionColor);

            sideStorySectionHeaders.add(sectionHeader);
        }

        for (SideStory sideStory : Database.getTable(SideStory.class))
        {
            SideStorySectionHeader sideStorySectionHeader = (SideStorySectionHeader) sideStorySectionHeaders.get(sideStory.Section);

            SideStoryHeader sideStoryHeader = new SideStoryHeader(sideStorySectionHeader, sideStory);
            sideStorySectionHeader.addSubItem(sideStoryHeader);

            SideStoryContent sideStoryContent = new SideStoryContent(sideStory);
            sideStoryHeader.addSubItem(sideStoryContent);
        }

        FlexibleAdapter<IFlexible> adapter = new FlexibleAdapter<>(sideStorySectionHeaders);
        adapter.setDisplayHeadersAtStartUp(true);
        adapter.setStickyHeaders(true);
        adapter.expandItemsAtStartUp();

        RecyclerView page_class = page.findViewById(R.id.side_story_list_view);
        page_class.setAdapter(adapter);
    }

    private void initializeClassPage(View view)
    {
        final String[] categoryColors = m_context.getResources().getStringArray(R.array.class_category_colors);

        List<ClassCategory> classCategoryList = Database.getTable(ClassCategory.class);
        List<UnitClass> unitClassList = Database.getTable(UnitClass.class);

        List<IFlexible> categoryHeaders = new ArrayList<>();
        for (int i = 0; i < classCategoryList.size(); i++)
        {
            ClassCategory classCategory = classCategoryList.get(i);
            ClassCategoryHeader categoryHeader = new ClassCategoryHeader(classCategory, categoryColors[i]);

            for (UnitClass unitClass : unitClassList)
            {
                if (unitClass.Category.equals(classCategory.Name))
                {
                    ClassHeader classHeader = new ClassHeader(categoryHeader, unitClass);
                    categoryHeader.addSubItem(classHeader);

                    ClassContent classContent = new ClassContent(unitClass);
                    classHeader.addSubItem(classContent);
                }
            }

            categoryHeaders.add(categoryHeader);
        }

        FlexibleAdapter<IFlexible> adapter = new FlexibleAdapter<>(categoryHeaders);
        adapter.setDisplayHeadersAtStartUp(true);
        adapter.setStickyHeaders(true);
        adapter.expandItemsAtStartUp();

        RecyclerView page_class = view.findViewById(R.id.class_list_view);
        page_class.setAdapter(adapter);
    }

    private void initializeLossPage(View view)
    {
        // Get table from db.
        List<Loss> table = Database.getTable(Loss.class);
        Collections.sort(table);

        List<IFlexible> model = new ArrayList<>();
        for (Loss loss : table)
        {
            model.add(new LossContent(loss));
        }

        // Initialize the Adapter
        FlexibleAdapter<IFlexible> adapter = new LossContent.Adapter(model, null, true);

        // Initialize the RecyclerView and attach the Adapter to it as usual
        RecyclerView recyclerView = view.findViewById(R.id.loss_list_view);
        recyclerView.setAdapter(adapter);

        // Then, add FastScroller to the RecyclerView
        m_fastScroller = view.findViewById(R.id.fast_scroller);
        m_fastScroller.setAutoHideEnabled(false);             //true is the default value
        //m_fastScroller.setAutoHideDelayInMillis(1000L);      //1000ms is the default value
        m_fastScroller.setHandleAlwaysVisible(true);        //false is the default value
        m_fastScroller.setIgnoreTouchesOutsideHandle(false); //false is the default value
        // 0 pixel is the default value. When > 0 it mimics the fling gesture
        m_fastScroller.setMinimumScrollThreshold(0);
        // OnScrollStateChangeListener remains optional
        //m_fastScroller.addOnScrollStateChangeListener(this);
        //m_fastScroller.removeOnScrollStateChangeListener(this);
        // The color (accentColor) is automatically fetched by the FastScroller constructor,
        // but you can change it at runtime:
        m_fastScroller.setBubbleAndHandleColor(Color.RED);

        // Finally, assign the Fastscroller to the Adapter
        adapter.setFastScroller(m_fastScroller);
    }

    private void initializeLicensePage(View view)
    {
        String[] licenses = getResources().getStringArray(R.array.licenses);
        String[] license_types = getResources().getStringArray(R.array.license_types);
        String[] license_url = getResources().getStringArray(R.array.license_agreement_url);

        List<IFlexible> licenseItemList = new ArrayList<>();

        for (int i = 0; i < licenses.length; ++i)
        {
            licenseItemList.add(new LicenseItem(licenses[i], license_types[i], license_url[i]));
        }

        FlexibleAdapter<IFlexible> adapter = new FlexibleAdapter<>(licenseItemList);

        RecyclerView recyclerView = view.findViewById(R.id.license_list_view);
        recyclerView.setAdapter(adapter);
    }
}
