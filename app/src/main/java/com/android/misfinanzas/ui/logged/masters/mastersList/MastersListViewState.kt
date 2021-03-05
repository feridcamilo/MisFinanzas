package com.android.misfinanzas.ui.logged.masters.mastersList

import com.android.misfinanzas.models.MasterModel

sealed class MastersListViewState {

    class PeopleLoaded(val people: List<MasterModel>) : MastersListViewState()
    class PlacesLoaded(val places: List<MasterModel>) : MastersListViewState()
    class CategoriesLoaded(val categories: List<MasterModel>) : MastersListViewState()
    class DebtsLoaded(val debts: List<MasterModel>) : MastersListViewState()
    class MastersFiltered(val masters: List<MasterModel>) : MastersListViewState()

}
