package ua.rodev.buttontoactionapp.domain

import ua.rodev.buttontoactionapp.core.Read
import ua.rodev.buttontoactionapp.core.Save

interface ActionsTimeUsageHistoryStorage {
    interface Mutable : Save<Map<String, Long>>, Read<Map<String, Long>>
}
