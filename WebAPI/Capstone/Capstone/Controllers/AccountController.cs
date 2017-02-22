using System;
using System.Collections.Generic;
using System.Net.Http;
using System.Security.Claims;
using System.Security.Cryptography;
using System.Threading.Tasks;
using System.Web.Http;
using System.Web.Http.ModelBinding;
using Microsoft.AspNet.Identity;
using Microsoft.AspNet.Identity.EntityFramework;
using Microsoft.AspNet.Identity.Owin;
using Microsoft.Owin.Security;
using Microsoft.Owin.Security.Cookies;
using Microsoft.Owin.Security.OAuth;
using Capstone.Models;
using Capstone.Providers;
using Capstone.Results;
using static Capstone.ApplicationUserManager;
using System.Web;
using System.Web.Http.Description;

namespace Capstone.Controllers
{
    [Authorize]
    [RoutePrefix("api/Account")]
    public class AccountController : ApiController
    {
        private ApplicationSignInManager _signInManager;
        private ApplicationUserManager _userManager;

        private const string LocalLoginProvider = "Local";

        public AccountController()
        {
        }

        public AccountController(ApplicationUserManager userManager, ApplicationSignInManager signInManager)
        {
            UserManager = userManager;
            SignInManager = signInManager;
        }

        public ApplicationSignInManager SignInManager
        {
            get
            {
                return _signInManager ?? Request.GetOwinContext().GetUserManager<ApplicationSignInManager>();
            }
            private set
            {
                _signInManager = value;
            }
        }

        public ApplicationUserManager UserManager
        {
            get
            {
                if (_userManager == null)
                {
                    _userManager = Request.GetOwinContext().GetUserManager<ApplicationUserManager>();
                }
                return _userManager;
            }
            private set
            {
                _userManager = value;
            }
        }

        //
        // POST: /Account/Login
        [HttpPost]
        [AllowAnonymous]
        [Route("Login")]
        [ResponseType(typeof(ResultModel))]
        public IHttpActionResult Login(LoginViewModel model)
        {
            var signManager = this.SignInManager;
            if (!ModelState.IsValid)
            {
                return Json(new ResultModel
                {
                    success = false,
                    message = "Tên tài khoản hoặc mật khẩu không đúng, xin thử lại!"
                });
            }
            var user = UserManager.Find(model.Username, model.Password);
            var result = signManager.PasswordSignIn(model.Username, model.Password, model.RememberMe, shouldLockout: false);
            if (result == SignInStatus.Success)
            {
                return Json(new ResultModel
                {
                    success = true,
                    message = "Đăng nhập thành công!",
                    obj = UserManager.GetRoles(user.Id),
                });
            }
            return Json(new ResultModel
            {
                success = false,
                message = "Tên tài khoản hoặc mật khẩu không đúng, xin thử lại!"
            });
        }

        [HttpPost]
        [AllowAnonymous]
        [Route("Register")]
        [ResponseType(typeof(ResultModel))]
        public IHttpActionResult Register(RegisterViewModel model)
        {
            if (ModelState.IsValid)
            {
                var user = new ApplicationUser() { UserName = model.Username, Email = model.Email };

                var result = this.UserManager.Create(user, model.Password);

                if (!result.Succeeded)
                {
                    if (this.UserManager.FindByName(model.Username) != null)
                    {
                        return Json(new ResultModel
                        {
                            success = false,
                            message = "Tài khoản " + model.Username + " đã tồn tại!"
                        });
                    }
                    if (this.UserManager.FindByEmail(model.Email) != null)
                    {
                        return Json(new ResultModel
                        {
                            success = false,
                            message = "Email " + model.Email + " đã tồn tại"
                        });
                    }
                    return Json(new ResultModel
                    {
                        success = false,
                        message = "Tạo người dùng thất bại, vui lòng liên hệ admin!"
                    });
                }

                var rs = UserManager.AddToRole(user.Id, "ActiveUser");

                if (!rs.Succeeded)
                {
                    return Json(new { success = false, message = rs.Errors });

                }
                //await Utils.PostNotiMessageToStores(stores, (int)NotifyMessageType.AccountChange);
                return Json(new ResultModel
                {
                    success = true,
                    message = "Tạo người dùng thành công"
                });

            }

            return Json(new { success = false, message = "Tạo người dùng thất bại" });
        }



        #region Helpers

        private IAuthenticationManager Authentication
        {
            get { return Request.GetOwinContext().Authentication; }
        }

        private IHttpActionResult GetErrorResult(IdentityResult result)
        {
            if (result == null)
            {
                return InternalServerError();
            }

            if (!result.Succeeded)
            {
                if (result.Errors != null)
                {
                    foreach (string error in result.Errors)
                    {
                        ModelState.AddModelError("", error);
                    }
                }

                if (ModelState.IsValid)
                {
                    // No ModelState errors are available to send, so just return an empty BadRequest.
                    return BadRequest();
                }

                return BadRequest(ModelState);
            }

            return null;
        }

        private class ExternalLoginData
        {
            public string LoginProvider { get; set; }
            public string ProviderKey { get; set; }
            public string UserName { get; set; }

            public IList<Claim> GetClaims()
            {
                IList<Claim> claims = new List<Claim>();
                claims.Add(new Claim(ClaimTypes.NameIdentifier, ProviderKey, null, LoginProvider));

                if (UserName != null)
                {
                    claims.Add(new Claim(ClaimTypes.Name, UserName, null, LoginProvider));
                }

                return claims;
            }

            public static ExternalLoginData FromIdentity(ClaimsIdentity identity)
            {
                if (identity == null)
                {
                    return null;
                }

                Claim providerKeyClaim = identity.FindFirst(ClaimTypes.NameIdentifier);

                if (providerKeyClaim == null || String.IsNullOrEmpty(providerKeyClaim.Issuer)
                    || String.IsNullOrEmpty(providerKeyClaim.Value))
                {
                    return null;
                }

                if (providerKeyClaim.Issuer == ClaimsIdentity.DefaultIssuer)
                {
                    return null;
                }

                return new ExternalLoginData
                {
                    LoginProvider = providerKeyClaim.Issuer,
                    ProviderKey = providerKeyClaim.Value,
                    UserName = identity.FindFirstValue(ClaimTypes.Name)
                };
            }
        }

        private static class RandomOAuthStateGenerator
        {
            private static RandomNumberGenerator _random = new RNGCryptoServiceProvider();

            public static string Generate(int strengthInBits)
            {
                const int bitsPerByte = 8;

                if (strengthInBits % bitsPerByte != 0)
                {
                    throw new ArgumentException("strengthInBits must be evenly divisible by 8.", "strengthInBits");
                }

                int strengthInBytes = strengthInBits / bitsPerByte;

                byte[] data = new byte[strengthInBytes];
                _random.GetBytes(data);
                return HttpServerUtility.UrlTokenEncode(data);
            }
        }

        #endregion
    }
}
