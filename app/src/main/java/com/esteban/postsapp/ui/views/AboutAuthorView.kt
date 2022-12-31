package com.esteban.postsapp.ui.views

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.widget.RelativeLayout
import com.esteban.postsapp.databinding.ViewAboutAutorBinding
import com.esteban.postsapp.domain.model.User
import kotlin.properties.Delegates

class AboutAuthorView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0,
) : RelativeLayout(context, attrs, defStyleAttr) {

    val binding: ViewAboutAutorBinding by lazy {
        ViewAboutAutorBinding.inflate(LayoutInflater.from(context), this, true)
    }

    var user: User? by Delegates.observable(null) { _, _, value ->
        value?.let {
            with(binding) {
                viewAboutAuthorName.text = it.getNameAndUserName()
                viewAboutAuthorWebsite.text = it.website
                viewAboutAuthorPhone.text = it.phone
                viewAboutAuthorCompany.text = it.company.name
                viewAboutAuthorCatchPhrase.text = it.company.catchPhrase
            }
        }
    }
}