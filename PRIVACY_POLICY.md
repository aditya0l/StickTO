# Privacy Policy for Note Flow

**Effective Date**: December 11, 2025  
**Last Updated**: December 11, 2025

---

## Overview

Note Flow ("the App") is an offline-first note-taking application that prioritizes your privacy. We do not collect, store, or share any personal information.

---

## Data Collection

**We collect ZERO personal data.**

### What Data is Stored Locally

- **Notes**: All notes and text content are stored locally in your device's private database (Room/SQLite)
- **Voice Recordings**: Voice notes are stored locally in your device's app-specific storage
- **Theme Preferences**: Your dark/light mode preference is stored locally in DataStore
- **All data is stored ONLY on your device and is NEVER uploaded to any server**

### What Happens to Your Data

- Your notes remain on your device only
- No data is transmitted to our servers or any third party
- Uninstalling the app permanently deletes all local data
- We have no access to your notes or voice recordings

---

## Firebase Remote Config

The app uses Firebase Remote Config (provided by Google LLC) solely for maintenance mode signaling. This service:

- **Does NOT collect personal information**
- Uses an anonymous device-level identifier for configuration delivery
- **Does NOT track user behavior, location, or activity**
- **Does NOT share data with third parties**
- Only delivers configuration flags (e.g., maintenance mode status)

Firebase Remote Config is used to ensure a smooth user experience by allowing us to temporarily disable the app for maintenance without requiring an app update.

---

## Permissions

### RECORD_AUDIO
- **Purpose**: Used only for voice note recording functionality
- **Data Storage**: All voice recordings are stored locally on your device
- **Data Transmission**: Voice recordings are NEVER uploaded or transmitted

### INTERNET
- **Purpose**: Required by Firebase Remote Config to check maintenance mode status
- **Data Transmission**: Only anonymous configuration requests to Firebase
- **No personal data transmitted**

---

## Data Deletion

Since all data is stored locally on your device:

- **To delete all notes and voice recordings**: Uninstall the app
- **To delete individual notes**: Use the delete function within the app
- **Server-side data**: No server-side data exists, so there is nothing to delete remotely

---

## Third-Party Services

The app uses **only** the following third-party service:

### Firebase Remote Config (Google LLC)
- **Purpose**: Maintenance mode signaling
- **Data Collected**: Anonymous device-level configuration identifier
- **Privacy Policy**: [Firebase Privacy Policy](https://firebase.google.com/support/privacy)

**We do NOT use**:
- Firebase Analytics
- Firebase Authentication
- Firebase Cloud Storage
- Firebase Firestore/Database
- Google Analytics
- Any advertising services
- Any crash reporting services
- Any user tracking services

---

## Children's Privacy

This app does not knowingly collect information from children under the age of 13. The app does not collect personal information from any users regardless of age.

---

## Data Security

### Local Data Security
- All notes are stored in your device's private app storage, protected by Android's sandboxing
- Voice recordings are stored in app-specific storage, inaccessible to other apps
- Data is encrypted at rest using Android's built-in encryption (if device encryption is enabled)

### Network Security
- All Firebase Remote Config requests use HTTPS encryption in transit
- No personal data is transmitted over the network

---

## Your Rights

Since we do not collect personal data, data subject rights (access, rectification, deletion, etc.) are not applicable. You have full control over your local data:

- **Access**: All notes are visible in the app
- **Edit**: Edit any note within the app
- **Delete**: Delete notes individually or uninstall the app to remove all data
- **Export**: Use Android's built-in backup functionality (if enabled on your device)

---

## Changes to This Privacy Policy

We may update this privacy policy from time to time. Any changes will be posted:
- Within the app (if significant changes are made)
- In future app updates on the Play Store

Continued use of the app after changes constitutes acceptance of the updated policy.

---

## Compliance

This privacy policy complies with:
- Google Play Store Data Safety requirements
- General Data Protection Regulation (GDPR) principles
- California Consumer Privacy Act (CCPA) principles
- Children's Online Privacy Protection Act (COPPA)

---

## Contact Us

If you have any questions about this privacy policy or the app's privacy practices, please contact us:

**Email**: [YOUR_EMAIL_HERE]  
**App Name**: Note Flow  
**Developer**: [YOUR_NAME/COMPANY_HERE]

---

## Summary

**In Plain English**:

Note Flow is a completely offline note-taking app. Everything you create stays on your device. We don't collect your data, we don't track you, and we don't sell anything to advertisers. The only internet connection is to check if the app needs maintenance (using Firebase Remote Config), which doesn't involve any personal information.

Your notes are yours, and they never leave your device.

---

**Effective Date**: December 11, 2025  
**Version**: 1.0

