const functions = require("firebase-functions");
const admin = require("firebase-admin");
admin.initializeApp();

/**
 * Deletes a user's Firebase Authentication record.
 * This is a callable function, meaning it must be invoked from your app.
 */
exports.deleteUserAccount = functions.https.onCall(async (data, context) => {
  // Check if the user calling the function is authenticated.
  if (!context.auth) {
    throw new functions.https.HttpsError(
      "unauthenticated",
      "The function must be called while authenticated."
    );
  }

  // Check if the authenticated user has the 'Manager' role.
  // This is a crucial security check.
  const callerUid = context.auth.uid;
  const callerDoc = await admin.firestore().collectionGroup("users")
      .where("uid", "==", callerUid).limit(1).get();

  if (callerDoc.empty || callerDoc.docs[0].data().role !== "Manager") {
      throw new functions.https.HttpsError(
        "permission-denied",
        "Only managers can delete users."
      );
  }

  const userToDeleteUid = data.uid;
  if (!userToDeleteUid || typeof userToDeleteUid !== "string") {
    throw new functions.https.HttpsError(
      "invalid-argument",
      "The function must be called with a 'uid' argument."
    );
  }

  try {
    // Use the Admin SDK to delete the user by their UID
    await admin.auth().deleteUser(userToDeleteUid);
    console.log(`Successfully deleted user ${userToDeleteUid}`);
    return { success: true, message: `User ${userToDeleteUid} deleted.` };
  } catch (error) {
    console.error(`Error deleting user ${userToDeleteUid}:`, error);
    throw new functions.https.HttpsError(
      "internal",
      `Failed to delete user: ${error.message}`
    );
  }
});