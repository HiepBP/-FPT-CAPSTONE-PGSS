using AutoMapper;
using Capstone.Models.Entities;
using Capstone.Models.Entities.Services;
using Capstone.ViewModels;
using System;
using System.Collections.Generic;
using System.Linq;
using System.Reflection;
using System.Text;
using System.Threading.Tasks;

namespace Capstone
{
    public static class ApiEndpoint
    {

        public static void Entry(Action<IMapperConfiguration> additionalMapperConfig, params Autofac.Module[] additionalModules)
        {
            Action<IMapperConfiguration> fullMapperConfig = q =>
            {
                ApiEndpoint.ConfigAutoMapper(q);

                if (additionalMapperConfig != null)
                {
                    additionalMapperConfig(q);
                }
            };

            SkyWeb.DatVM.Mvc.Autofac.AutofacInitializer.Initialize(
                Assembly.GetExecutingAssembly(),
                typeof(PGSSEntities),
                new MapperConfiguration(fullMapperConfig),
                additionalModules);
        }



        private static void ConfigAutoMapper(IMapperConfiguration config)
        {
            config.CreateMissingTypeMaps = true;

            config.AllowNullDestinationValues = false;
        }

    }
}
