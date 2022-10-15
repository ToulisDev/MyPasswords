# Password Manager Android Application
<a name="readme-top"></a>

<!-- PROJECT SHIELDS -->
<!--
*** https://www.markdownguide.org/basic-syntax/#reference-style-links
-->
[![Issues][issues-shield]][issues-url]
[![APACHE 2.0 License][license-shield]][license-url]

<!-- PROJECT LOGO -->
<br />
<div align="center">
  <a href="https://github.com/ToulisDev/MyPasswords">
    <img src="images/logo.png" alt="Logo" width="80" height="80">
  </a>

<h3 align="center">Password Manager Android Application</h3>

  <p align="center">
    A Password Manager for Android devices.
    <br />
    <a href="https://github.com/ToulisDev/MyPasswords"><strong>Explore the docs »</strong></a>
    <br />
    <br />
    <a href="https://github.com/ToulisDev/MyPasswords/issues">Report Bug</a>
    ·
    <a href="https://github.com/ToulisDev/MyPasswords/issues">Request Feature</a>
  </p>
</div>

<!-- TABLE OF CONTENTS -->
<details>
  <summary>Table of Contents</summary>
  <ol>
    <li>
      <a href="#about-the-application">About The Application</a>
      <ul>
        <li><a href="#built-with">Built With</a></li>
      </ul>
    </li>
    <li>
      <a href="#getting-started">Getting Started</a>
      <ul>
        <li><a href="#prerequisites">Prerequisites</a></li>
        <li><a href="#installation">Installation</a></li>
      </ul>
    </li>
    <li><a href="#usage">Usage</a></li>
    <li><a href="#contributing">Contributing</a></li>
    <li><a href="#contact">Contact</a></li>
  </ol>
</details>

<!-- ABOUT THE APPLICATION -->
## About The Application

It’s an android application which communicates with the <a href="https://github.com/ToulisDev/PasswordManagerRestApi">Password Manager Rest-API</a>. After a successful
login, the user can create/save passwords(Username,Password,Date-Created,Site) with a corresponding
logo icon. The logo is downloded locally on the device by using a simple web-crawl algorithm to Google
Images. Application created using Java and XML on Android Studio.

### Built With

* [![Java][Java]][Java-url]
* [![AndroidStudio][AndroidStudio]][AndroidStudio-url]

<p align="right">(<a href="#readme-top">back to top</a>)</p>

<!-- GETTING STARTED -->
## Getting Started

In order to get up and running the Application, you need to build the project using Android Studio and have an instance of <a href="https://github.com/ToulisDev/PasswordManagerRestApi">Password Manager Rest-API</a> up and running on a server. You will also have to update `ConnectionHelper.java` with your server url.

### Prerequisites

* [Android Studio](https://developer.android.com/studio)


<!-- USAGE EXAMPLES -->
## Usage

In order to use the application, you must register an account to the database and login with the account you just created.

<div style="display:flex;">
  <img src="https://github.com/ToulisDev/MyPasswords/blob/main/images/screenshot-register.png?raw=true" alt="" data-canonical-src="https://github.com/ToulisDev/MyPasswords/blob/main/images/screenshot-register.png?raw=true" width="270" height="540" />
  <img src="https://github.com/ToulisDev/MyPasswords/blob/main/images/screenshot-login.png?raw=true" alt="" data-canonical-   src="https://github.com/ToulisDev/MyPasswords/blob/main/images/screenshot-login.png?raw=true" width="270" height="540" />
</div>

<br/>

After a successful login, you can create a new password by clicking on **ADD** button and after filling the fields you hit **SAVE**.

<div style="display:flex;">
  <img src="https://github.com/ToulisDev/MyPasswords/blob/main/images/screenshot-main.png?raw=true" alt="" data-canonical-src="https://github.com/ToulisDev/MyPasswords/blob/main/images/screenshot-main.png?raw=true" width="270" height="540" />
  <img src="https://github.com/ToulisDev/MyPasswords/blob/main/images/screenshot-add.png?raw=true" alt="" data-canonical-   src="https://github.com/ToulisDev/MyPasswords/blob/main/images/screenshot-add.png?raw=true" width="270" height="540" />
  <img src="https://github.com/ToulisDev/MyPasswords/blob/main/images/screenshot-netflix.png?raw=true" alt="" data-canonical-   src="https://github.com/ToulisDev/MyPasswords/blob/main/images/screenshot-netflix.png?raw=true" width="270" height="540" />
</div>

<br/>

After saving the password, it should show up on main menu. By clicking the password you created, a pop-up window shows on which you can copy on clipboard the username (CLIPBOARD button), show the password (EYE icon) or edit the password (PENCIL icon). By hitting the edit button you can change or delete the password.

<div style="display:flex;">
  <img src="https://github.com/ToulisDev/MyPasswords/blob/main/images/screenshot-netflix-main.png?raw=true" alt="" data-canonical-src="https://github.com/ToulisDev/MyPasswords/blob/main/images/screenshot-netflix-main.png?raw=true" width="270" height="540" />
  <img src="https://github.com/ToulisDev/MyPasswords/blob/main/images/screenshot-editpop.png?raw=true" alt="" data-canonical-   src="https://github.com/ToulisDev/MyPasswords/blob/main/images/screenshot-editpop.png?raw=true" width="270" height="540" />
  <img src="https://github.com/ToulisDev/MyPasswords/blob/main/images/screenshot-edit.png?raw=true" alt="" data-canonical-   src="https://github.com/ToulisDev/MyPasswords/blob/main/images/screenshot-edit.png?raw=true" width="270" height="540" />

<p align="right">(<a href="#readme-top">back to top</a>)</p>

<!-- CONTRIBUTING -->
## Contributing

Contributions are what make the open source community such an amazing place to learn, inspire, and create. Any contributions you make are **greatly appreciated**.

If you have a suggestion that would make this better, please fork the repo and create a pull request. You can also simply open an issue with the tag "enhancement".
Don't forget to give the project a star! Thanks again!

1. Fork the Project
2. Create your Feature Branch (`git checkout -b feature/AmazingFeature`)
3. Commit your Changes (`git commit -m 'Add some AmazingFeature'`)
4. Push to the Branch (`git push origin feature/AmazingFeature`)
5. Open a Pull Request

<p align="right">(<a href="#readme-top">back to top</a>)</p>

<!-- CONTACT -->
## Contact

Aggelos S. - aggelos_sachtouris@hotmail.com

Project Link: [https://github.com/ToulisDev/MyPasswords](https://github.com/ToulisDev/MyPasswords)

<p align="right">(<a href="#readme-top">back to top</a>)</p>

<!-- MARKDOWN LINKS & IMAGES -->
<!-- https://www.markdownguide.org/basic-syntax/#reference-style-links -->
[issues-shield]: https://img.shields.io/github/issues/ToulisDev/MyPasswords?style=for-the-badge
[issues-url]: https://github.com/ToulisDev/MyPasswords/issues
[license-shield]: https://img.shields.io/github/license/ToulisDev/MyPasswords?style=for-the-badge
[license-url]: https://github.com/ToulisDev/MyPasswords/blob/main/LICENSE.md
[product-register]: images/screenshot-register.png
[product-main]: images/screenshot-main.png
[product-add]: images/screenshot-add.png
[product-netflix]: images/screenshot-netflix.png
[product-main-netflix]: images/screenshot-netflix-main.png
[product-edit-pop]: images/screenshot-editpop.png
[product-edit]: images/screenshot-edit.png
[product-main-new]: images/screenshot-main-edit.png
[AndroidStudio]: https://img.shields.io/badge/Android_Studio-3DDC84?style=for-the-badge&logo=android%20studio&logoColor=white
[AndroidStudio-url]: https://developer.android.com/studio
[Java]: https://img.shields.io/badge/Java-FF7800?style=for-the-badge
[Java-url]: https://www.java.com/en/
