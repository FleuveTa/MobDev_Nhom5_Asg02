package com.example.prj02_healthy_plan.ui.theme

import androidx.compose.runtime.mutableDoubleStateOf
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.FirebaseFirestore
import org.junit.Before
import org.junit.Test
import org.mockito.Mock
import org.mockito.Mockito.mock
import org.mockito.Mockito.`when`
import org.mockito.kotlin.verify


class UserInforUIKtTest {

    @Mock
    private lateinit var mockAuth: FirebaseAuth
    private lateinit var mockUser: FirebaseUser
    private lateinit var mockDb: FirebaseFirestore

    @Mock
    private lateinit var mockCollection: CollectionReference

    @Mock
    private lateinit var mockDocument: DocumentReference


    @Before
    fun setUp() {
        mockAuth = mock(FirebaseAuth::class.java)
        mockUser = mock(FirebaseUser::class.java)
        mockDb = mock(FirebaseFirestore::class.java)
        mockCollection = mock(CollectionReference::class.java)
        mockDocument = mock(DocumentReference::class.java)

        `when`(mockAuth.currentUser).thenReturn(mockUser)
        `when`(mockUser.uid).thenReturn("test_uid")
        `when`(mockDb.collection("users")).thenReturn(mockCollection)
        `when`(mockCollection.document("test_uid")).thenReturn(mockDocument)
    }


    @Test
    fun testSaveButtonClick() {
        val nameValue = mutableStateOf("Dtest")
        val heightValue = mutableIntStateOf(180)
        val genderValue = mutableIntStateOf(0)
        val dobValue = mutableStateOf("17-11-2003")
        val activityLevelValue = mutableIntStateOf(1)
        val weeklyGoalValue = mutableStateOf("0.5")
        val caloriesGoalValue = mutableIntStateOf(2)
        val nutrientGoalValue = mutableIntStateOf(1)
        val weightValue = mutableIntStateOf(67)
        val targetWeightValue = mutableIntStateOf(58)
        val goalValue = mutableIntStateOf(1)

        saveUserChanges(
            mockUser.uid,
            mockDb,
            nameValue,
            heightValue,
            genderValue,
            dobValue,
            activityLevelValue,
            weeklyGoalValue,
            caloriesGoalValue,
            nutrientGoalValue,
            weightValue,
            targetWeightValue,
            goalValue
        )

        verify(mockDocument).set(
            hashMapOf(
                "fullName" to nameValue.value,
                "height" to heightValue.intValue,
                "gender" to genderValue.intValue,
                "dob" to dobValue.value,
                "activityLevel" to activityLevelValue.intValue,
                "weeklyGoal" to weeklyGoalValue.value,
                "caloriesGoal" to caloriesGoalValue.intValue,
                "nutrientGoal" to nutrientGoalValue.intValue,
                "weight" to weightValue.intValue,
                "targetWeight" to targetWeightValue.intValue,
                "goal" to goalValue.intValue,
            ))
    }


}