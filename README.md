# ID Card Data Extractor

Text Extraction from Aadhaar Card,Pan Card for Indian Citizens using on-device(Android) machine learning. 
Since Aadhar Card and Pan Card contain sensitive info about users, sending images of these to cloud to extract data
raises security concerns particulary if the servers are located outside India.  
However, here all the text is extracted using machine learning implemented on device.All computations are happening on device and no server/internet connection is required once the trained ML model has been downloaded.

<img src="https://lh3.googleusercontent.com/9JisclTeXnC14T5fzUkMyTkGXZ4rSWtZ2QxLBqRJj1Ce6nG52RVj0KGZDOIl16FKqIy48SAF-hPMhB-4qqeqFIKUetpoWYzcsaeQK1_eJG3v17cX5UHaWOB5xtqHUtVU8GfeJT2CufDW8J-wGIQg4E1jzLBE95GRfiEtNzHL79BIiaB0hLx9gMKuoAulHIM9c0pl9XWj0bJUuFrFgjzNce4V8KvWsxyVPKoOGjTOvCCjHPS7jxgck9j0fwxpKS1KVVtFT-ycPLH8DkHTCk6QZHQM_L-TYKPykKrhqXklNZf1aXIU3-ptahHbuG-eTgXVZ_zos3Gel_X-cyNg7FpyrdzrbYYRT0_ObWbYtiHTpVBw7WSDWDLM8iFJvzFWF6jazxQWsSiSQXgE6nZzoCtUlYC4bKLsfLtWa5-MN1Qno8k2eMyZ489DO5IKIUynHZUDHUkL6cwDyN1SlZxhJkT7Nd3PFHmFG_vOtZ99BH7gWVuXFqV4cuMUdfGB9fUkzeYnp_g0Ztw5h3HYtkUEsHYwEx2q9gRhL3F-ps-3-mbsfnWQR5t7LsiiAQGrQt1iFFdoNjSKX9ntLj0Zk0WlYsRyCbhtHkmqdjh9ne1tTGlhsf37ihhuQBt-ljjXNtFYsqSSkmGdHYcqooMJUfp_4eNZvOnCGOIh5NMsmA=w2160-h1384-no" align="center" width="1000">

## Supported Identity Cards (for Indian Citizens only)

1.Aadhar Card  
2.Pan Card  
The above 2 were carefully selected as they are one of the popular proofs of identity and address used in India and also because they follow a stricly defined template as they have been published over the years in comparison to Voter-ID cards which has several variations.  
Also i will add support for Indian Passports in the future.  

## Architecture

Here a pretrained ML Model is used whose output is fed to a data post processing layer(in android app). Once data is processed, the android app shows the relevant extracted details.  
I am not an expert in data science ,however if someone is really looking to build his own Machine learning model,[Tensorflow lite is place to start.](https://www.tensorflow.org/mobile/tflite/)  
You can make a custom Tensorflow model,convert it to TF lite and then embed it into the android app.  

<img src="https://lh3.googleusercontent.com/Fx2RsHY1XgRr2NHhBXcwaWw2gNL19hJJ7OkFLj75AOmPxPTJays0GYX7EQEaLPkX9X-Aofx1UIDvfo4Hgh1GR7B_UmwhNtiqRpOkjiqrg1lFCAjp7ljHYxRRsLXK_rLOYfJvEO6p9iq5uiY_fU99QiuOnDj9Cu8TOxeDa5CV4qgVLlpvOdfXtkrRZQTSjY--dJUpPtBBaOBCCEq5nBccYO5fHf6lJzBNST4Ac7WMyvxAZPnyAw7m8h4xG_c3d0n50kOA3l7HHoHHmwOPxExDhQVNFDzRNEb-b08FmsAH-abbU21_9jH6ZVMu_cBDooMpUtXjFIS527zO1IEPp0SEiDE8rvVVON0--iqQMM1jYSo7aGwqJSi_mybuGUDuno3tUqBdanyL3439ny94InCYYcHyWNYAS2-HzeiGQxFdU0KA-ySSEvF1Tz79XSQuG7rgLzIU3Ke0y72bm0dy-33SCloNGfqjr6oTLDsWuA95htGMEs0EGuMH6YXQFT3VgYUitqiswWaE_snmKnASErJxmEh7ZE41MDxq6Zp4pGueumFyVo0udqTBoAHUdf1lBqr5ZomilyxXxrtCr_fcNNLWmQW_G1_xstwmvwaMYeX1=w797-h461-no" align="center" width="800">


## Setup

1.Download your own google-services.json file and put in under /app directory.If you need more info check [add Firebase to Android](https://firebase.google.com/docs/android/setup).  
2.After successfully connecting to firebase, a pre-trained Mahine Learning model for text extraction is downloaded using MLKit.  
3.If everything is in place,you should be able to run the app for data extraction on an android phone.  
4.The app has three modes or activities (Select appropriate mode):  
  I : Aadhar Card : here output of pretrained ML Model from MLKit is processed specifically for Aadhaar Cards.  
  II: Pan Card : here output of pretrained ML Model from MLKit is processed specifically for Pan Cards.   
  III: Generic ID Card : Here standard output of ML model from MLKit is shown.    

**IMPORTANT**: This app was tested on Google Pixel running Android 8.1 which privides support for Neural Networks API to improve performance of
performing machine learning tasks on-device. Older devices may take slightly longer times to generate output.

## Contribution  

If you want to contribute by either by adding support for more types of identity documents or building a custom ML model from scratch, Feel free to contact me at akshitgupta1695@gmail.com

