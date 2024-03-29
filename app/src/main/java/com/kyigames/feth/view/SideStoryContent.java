package com.kyigames.feth.view;

import android.view.View;
import android.widget.TextView;

import com.kyigames.feth.R;
import com.kyigames.feth.model.SideStory;
import com.kyigames.feth.utils.ResourceUtils;

import java.util.List;

import eu.davidea.flexibleadapter.FlexibleAdapter;
import eu.davidea.flexibleadapter.items.AbstractFlexibleItem;
import eu.davidea.flexibleadapter.items.IFlexible;
import eu.davidea.viewholders.FlexibleViewHolder;

public class SideStoryContent extends AbstractFlexibleItem<SideStoryContent.ViewHolder>
{
    private String m_name;
    private String m_clearRewards;
    private String m_missionRewards;
    private String m_knightsRewards;
    private String m_fieldItems;

    public SideStoryContent(SideStory sideStory)
    {
        m_name = sideStory.Name;
        m_clearRewards = ResourceUtils.getListItemText(sideStory.ClearRewards);
        m_knightsRewards = ResourceUtils.getListItemText(sideStory.RewardKnights);

        if (sideStory.Mission == null)
        {
            m_missionRewards = "없음";
        } else
        {
            m_missionRewards = sideStory.Mission
                    + "에 따라 "
                    + ResourceUtils.getListItemText(sideStory.MissionRewards);
        }

        m_fieldItems = ResourceUtils.getListItemText(sideStory.FieldItems);
    }

    @Override
    public boolean equals(Object o)
    {
        if (o instanceof SideStoryContent)
        {
            SideStoryContent sideStoryContent = (SideStoryContent) o;
            return m_name.equals(sideStoryContent.m_name);
        }
        return false;
    }

    @Override
    public int getLayoutRes()
    {
        return R.layout.side_story_content;
    }

    @Override
    public ViewHolder createViewHolder(View view, FlexibleAdapter<IFlexible> adapter)
    {
        return new ViewHolder(view, adapter);
    }


    class ViewHolder extends FlexibleViewHolder
    {
        TextView ClearRewards;
        TextView MissionRewards;
        TextView KnightsRewards;
        TextView FieldItems;

        public ViewHolder(View view, FlexibleAdapter adapter)
        {
            super(view, adapter, false);

            ClearRewards = view.findViewById(R.id.side_story_content_clear_reward);
            MissionRewards = view.findViewById(R.id.side_story_content_mission_reward);
            KnightsRewards = view.findViewById(R.id.side_story_content_knights_reward);
            FieldItems = view.findViewById(R.id.side_story_content_field_items);
        }
    }

    @Override
    public void bindViewHolder(FlexibleAdapter<IFlexible> adapter, ViewHolder holder, int position, List<Object> payloads)
    {
        holder.ClearRewards.setText(m_clearRewards);
        holder.MissionRewards.setText(m_missionRewards);
        holder.KnightsRewards.setText(m_knightsRewards);
        holder.FieldItems.setText(m_fieldItems);
    }
}
