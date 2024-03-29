package com.kyigames.feth.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.DrawableRes;
import androidx.constraintlayout.widget.ConstraintLayout;

import com.kyigames.feth.R;
import com.kyigames.feth.model.ISkillInfo;

public class SkillInfoView extends ConstraintLayout
{
    private ImageView m_icon;
    private TextView m_name;
    private TextView m_desc;

    public SkillInfoView(Context context)
    {
        super(context);
        initView();
    }

    public SkillInfoView(Context context, AttributeSet attrs)
    {
        super(context, attrs);
        initView();
    }

    public SkillInfoView(Context context, AttributeSet attrs, int defStyleAttr)
    {
        super(context, attrs, defStyleAttr);
        initView();
    }

    private void initView()
    {
        LayoutInflater inflater = LayoutInflater.from(getContext());
        View view = inflater.inflate(R.layout.skill_info_view, this, false);
        addView(view);

        m_icon = view.findViewById(R.id.class_ability_icon);
        m_name = view.findViewById(R.id.class_ability_name);
        m_desc = view.findViewById(R.id.class_ability_desc);
    }

    public void setInfo(ISkillInfo skillInfo)
    {
        setIcon(skillInfo.getIcon());
        setName(skillInfo.getName());
        setDescription(skillInfo.getDescription());
    }

    public void setIcon(@DrawableRes int iconRes)
    {
        m_icon.setImageResource(iconRes);
    }

    public void setName(String name)
    {
        m_name.setText(name);
    }

    public void setDescription(String description)
    {
        m_desc.setText(description);
        m_desc.invalidate();
    }
}
